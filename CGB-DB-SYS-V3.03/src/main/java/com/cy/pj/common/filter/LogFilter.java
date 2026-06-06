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
 * 日志拦截过滤器
 */
public class LogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化逻辑
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        long startTime = System.currentTimeMillis();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);

        try {
            chain.doFilter(httpRequest, responseWrapper);

            long duration = System.currentTimeMillis() - startTime;
            String responseBody = responseWrapper.getResponseBody();

            // 记录完整的请求-响应日志
            log.info("API调用完成 | URL: {} | Method: {} | Status: {} | Duration: {}ms | Response: {}",
                    httpRequest.getRequestURI(),
                    httpRequest.getMethod(),
                    httpResponse.getStatus(),
                    duration,
                    responseBody);

            // 设置统一的字符编码
            responseWrapper.setCharacterEncoding("UTF-8");
            responseWrapper.setContentType("application/json;charset=UTF-8");
            responseWrapper.writeToOriginalResponse(httpResponse);

        } finally {
            // 确保即使异常也能记录日志
        }
    }


}
