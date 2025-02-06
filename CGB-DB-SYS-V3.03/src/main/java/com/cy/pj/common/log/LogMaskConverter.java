package com.cy.pj.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Pattern;

/**
 * 日志脱敏转换器
 */
public class LogMaskConverter extends ClassicConverter {
    /**
     * 手机号正则表达式
     */
    private static final Pattern PHONE_REGEX = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");

    /**
     * 邮箱正则表达式
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("(\\w{3})@(\\w+)\\.\\w+");

    @Override
    public String convert(ILoggingEvent event) {
        String msg = event.getFormattedMessage();
        msg = PHONE_REGEX.matcher(msg).replaceAll("$1****$2");
        msg = EMAIL_REGEX.matcher(msg).replaceAll("$1***@$2.***");
        return msg;
    }
}
