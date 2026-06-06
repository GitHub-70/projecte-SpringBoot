package com.cy.pj.common.filter;

import com.cy.pj.common.response.ResponseWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * 响应体拦截过滤器
 */
public class ResponseBodyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ResponseBodyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化逻辑
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 创建响应包装器
        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);

        try {
            // 对于大文件下载，不建议使用此方案，直接返回原始响应
            if (isFileDownload(httpResponse)) {
                // 直接传递原始响应，不包装
                chain.doFilter(request, response);
                return;
            }

            // 对于异步请求（SSE、WebSocket），此方案不适用
            if (isAsyncRequest(httpRequest)) {
                chain.doFilter(request, response);
                return;
            }

            // 继续执行过滤器链，响应会被写入包装器
            chain.doFilter(httpRequest, responseWrapper);

            // 获取原始响应体
            String originalBody = responseWrapper.getResponseBody();
            log.info("原始响应体: {}", originalBody);

            // 修改响应体（示例：添加额外字段）
            String modifiedBody = modifyResponseBody(originalBody);
            log.info("修改后响应体: {}", modifiedBody);

            // 设置修改后的响应体
            responseWrapper.setResponseBody(modifiedBody);

            // 将修改后的响应写入原始响应
            responseWrapper.writeToOriginalResponse(httpResponse);

        } catch (Exception e) {
            log.error("处理响应体失败", e);
            throw e;
        }
    }

    /**
     * 判断是否为异步请求
     * @param httpRequest
     * @return boolean
     */
    private boolean isAsyncRequest(HttpServletRequest httpRequest) {
        if (httpRequest.getDispatcherType().equals(DispatcherType.ASYNC)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为文件下载请求
     * @param response
     * @return boolean
     */
    private boolean isFileDownload(HttpServletResponse response) {
        String contentType = response.getContentType();
        return contentType != null &&
                (contentType.contains("application/octet-stream") ||
                        contentType.contains("application/pdf"));
    }

    /**
     * 修改响应体的业务逻辑
     */
    private String modifyResponseBody(String originalBody) {
        if (originalBody == null || originalBody.isEmpty()) {
            return originalBody;
        }

        // 示例 1：JSON 响应添加时间戳
        if (originalBody.trim().startsWith("{")) {
            // 移除最后一个 }
            String content = originalBody.trim();
            if (content.endsWith("}")) {
                content = content.substring(0, content.length() - 1);
                // 添加时间戳字段
                return content + ",\"timestamp\":" + System.currentTimeMillis() + "}";
            }
        }

        // 示例 2：数据脱敏（隐藏手机号中间4位）
        return originalBody.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

        // 示例 3：响应加密（需要配合前端解密）
        // return encrypt(originalBody);
    }

    @Override
    public void destroy() {
        // 销毁逻辑
    }

    /**
     * 统一添加时间戳和版本号
     * @param originalBody
     * @return String
     */
    private String modifyResponseBody2(String originalBody) {
        if (originalBody == null || !originalBody.startsWith("{")) {
            return originalBody;
        }

        try {
            // 使用 Jackson 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(originalBody);

            // 如果是对象类型，添加字段
            if (jsonNode.isObject()) {
                ((ObjectNode) jsonNode).put("serverTime", System.currentTimeMillis());
                ((ObjectNode) jsonNode).put("version", "1.0.0");
                return mapper.writeValueAsString(jsonNode);
            }
        } catch (Exception e) {
            log.warn("JSON 处理失败，返回原始响应", e);
        }

        return originalBody;
    }


    /**
     * 敏感数据脱敏
     * @param body
     * @return String
     */
    private String maskSensitiveData(String body) {
        if (body == null) {
            return null;
        }

        // 手机号脱敏：13812345678 -> 138****5678
        body = body.replaceAll("\"phone\":\"(\\d{3})\\d{4}(\\d{4})\"", "\"phone\":\"$1****$2\"");

        // 身份证脱敏：110101199001011234 -> 110101********1234
        body = body.replaceAll("\"idCard\":\"(\\d{6})\\d{8}(\\d{4})\"", "\"idCard\":\"$1********$2\"");

        // 邮箱脱敏：test@example.com -> t***@example.com
        body = body.replaceAll("\"email\":\"(\\w)[^@]*(@[^\"]+)\"", "\"email\":\"$1***$2\"");

        return body;
    }


    /**
     * 响应加密
     * @param body
     * @return String
     */
    private String encryptResponse(String body) {
        try {
            // AES 加密密钥（实际应从配置中心获取）
            String key = "1234567890123456"; // 16字节
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(body.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            log.error("响应加密失败", e);
            return body; // 加密失败返回原文
        }
    }


}
