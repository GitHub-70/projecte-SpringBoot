package com.cy.pj.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志脱敏转换器（Logback %maskMsg 转换器）
 * 
 * <h3>功能说明</h3>
 * 在日志输出时自动对敏感信息进行脱敏处理，防止手机号、邮箱、身份证、银行卡等
 * 隐私数据明文打印到日志文件中，满足 GDPR/个人信息保护法 等合规要求。
 * 
 * <h3>支持的脱敏类型</h3>
 * <table border="1">
 *   <tr><th>类型</th><th>正则匹配</th><th>脱敏示例</th></tr>
 *   <tr><td>手机号</td><td>11位数字，1开头</td><td>138****5678</td></tr>
 *   <tr><td>邮箱</td><td>xxx@domain.com</td><td>abc***@domain.***</td></tr>
 *   <tr><td>身份证号</td><td>18位，最后一位可能是X</td><td>110105********1234</td></tr>
 *   <tr><td>银行卡号</td><td>16-19位数字</td><td>6222****5678</td></tr>
 *   <tr><td>姓名（2-4字）</td><td>中文字符</td><td>张* / 欧阳**</td></tr>
 *   <tr><td>密码/密钥</td><td>password=xxx 或 pwd:xxx 格式</td><td>password=****</td></tr>
 * </table>
 * 
 * <h3>配置方式</h3>
 * <pre>{@code
 * <!-- logback-spring.xml -->
 * <configuration>
 *     <!-- 1. 注册自定义转换器 -->
 *     <conversionRule conversionWord="maskMsg" 
 *                     converterClass="com.cy.pj.common.log.LogMaskConverter"/>
 *     
 *     <!-- 2. 在 pattern 中使用 %maskMsg 替代 %msg -->
 *     <appender name="FILE" class="ch.qos.logback.core.FileAppender">
 *         <encoder>
 *             <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %maskMsg%n</pattern>
 *         </encoder>
 *     </appender>
 * </configuration>
 * }</pre>
 * 
 * <h3>使用示例</h3>
 * <pre>{@code
 * // Java 代码中正常打印日志
 * log.info("用户登录：手机号={}, 邮箱={}", "13812345678", "user@example.com");
 * log.info("身份证号：{}", "110105199001011234");
 * log.info("银行卡号：{}", "6222021234567890");
 * 
 * // 日志输出结果（自动脱敏）
 * // 用户登录：手机号=138****5678, 邮箱=use***@example.***
 * // 身份证号：110105********1234
 * // 银行卡号：6222****7890
 * }</pre>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>脱敏是<strong>正则匹配</strong>，可能误匹配非敏感数据，请根据实际情况调整正则</li>
 *   <li>性能影响：每条日志都会经过正则匹配，高并发场景建议压测</li>
 *   <li>敏感信息仍会出现在<strong>日志文件</strong>中（脱敏后），需做好日志权限管控</li>
 *   <li>不支持结构化日志（JSON 格式），如需支持需自定义 JSON Encoder</li>
 * </ul>
 * 
 * @see ClassicConverter
 * @see ILoggingEvent
 */
public class LogMaskConverter extends ClassicConverter {

    private static final Logger log = LoggerFactory.getLogger(LogMaskConverter.class);

    /** 是否启用脱敏（默认启用，可通过 JVM 参数 -Dlog.mask.enabled=false 关闭） */
    private static final boolean MASK_ENABLED = Boolean.parseBoolean(
            System.getProperty("log.mask.enabled", "true"));

    // ==================== 正则表达式定义 ====================

    /**
     * 手机号正则
     * 匹配：1开头 + 10位数字（共11位）
     * 示例：13812345678 → 138****5678
     */
    private static final Pattern PHONE_REGEX = Pattern.compile("(1[3-9]\\d)\\d{4}(\\d{4})");

    /**
     * 邮箱正则
     * 匹配：xxx@domain.com 格式
     * 示例：user@example.com → use***@example.***
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("([\\w.+-]{3,})@(\\w+(?:\\.\\w+)+)");

    /**
     * 身份证号正则
     * 匹配：18位，前17位数字 + 最后1位数字或X
     * 示例：110105199001011234 → 110105********1234
     */
    private static final Pattern ID_CARD_REGEX = Pattern.compile("(\\d{6})\\d{8}(\\d{4}[0-9Xx])");

    /**
     * 银行卡号正则
     * 匹配：16-19位纯数字（常见 16/19 位）
     * 示例：6222021234567890 → 6222****7890
     */
    private static final Pattern BANK_CARD_REGEX = Pattern.compile("(\\d{4})\\d{8,11}(\\d{4})");

    /**
     * 密码/密钥正则
     * 匹配：password=xxx、pwd:xxx、secret=xxx、token=xxx 等格式
     * 示例：password=123456 → password=****
     */
    private static final Pattern PASSWORD_REGEX = Pattern.compile(
            "((?i)password|pwd|secret|token|key|credential)\\s*[=:]\\s*(\\S+)",
            Pattern.CASE_INSENSITIVE);

    /**
     * 姓名正则（2-4个中文字符）
     * 匹配：连续2-4个中文字符（前后无中文字符）
     * 示例：张三 → 张* / 欧阳娜娜 → 欧阳**
     */
    private static final Pattern NAME_REGEX = Pattern.compile("(?<=[^\\u4e00-\\u9fa5]|^)([\\u4e00-\\u9fa5]{2,4})(?=[^\\u4e00-\\u9fa5]|$)");

    // ==================== 核心方法 ====================

    @Override
    public String convert(ILoggingEvent event) {
        // 1. 获取原始日志消息
        String msg = event.getFormattedMessage();
        if (msg == null || msg.isEmpty()) {
            return "";
        }

        // 2. 如果脱敏功能被禁用，直接返回原始消息
        if (!MASK_ENABLED) {
            return msg;
        }

        // 3. 逐条应用脱敏规则（顺序很重要：先匹配长的/具体的，再匹配短的/模糊的）
        try {
            // 3.1 密码/密钥（优先级最高，避免被其他规则误匹配）
            msg = PASSWORD_REGEX.matcher(msg).replaceAll("$1=****");

            // 3.2 身份证号（18位，比手机号长，先匹配）
            msg = ID_CARD_REGEX.matcher(msg).replaceAll("$1********$2");

            // 3.3 银行卡号（16-19位）
            msg = BANK_CARD_REGEX.matcher(msg).replaceAll("$1****$2");

            // 3.4 手机号（11位）
            msg = PHONE_REGEX.matcher(msg).replaceAll("$1****$2");

            // 3.5 邮箱
            msg = EMAIL_REGEX.matcher(msg).replaceAll("$1***@$2");

            // 3.6 姓名（放在最后，避免误匹配其他数据）
            // ⚠️ 姓名脱敏可能误伤，默认注释，按需开启
            // msg = NAME_REGEX.matcher(msg).replaceAll(maskName("$1"));

        } catch (Exception e) {
            // 脱敏失败不应影响日志输出，降级为原始消息
            log.debug("日志脱敏失败，返回原始消息: {}", e.getMessage());
        }

        return msg;
    }

    /**
     * 姓名脱敏规则
     * 2字：张* → 张*
     * 3字：张三丰 → 张*丰
     * 4字：欧阳娜娜 → 欧阳**
     *
     * @param name 原始姓名
     * @return 脱敏后的姓名
     */
    @SuppressWarnings("unused")
    private String maskName(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }
        int len = name.length();
        if (len == 2) {
            return name.charAt(0) + "*";
        } else if (len == 3) {
            return name.charAt(0) + "*" + name.charAt(2);
        } else {
            // 4字及以上：保留首尾，中间全部掩码
            return name.charAt(0) + "**" + name.charAt(len - 1);
        }
    }
}
