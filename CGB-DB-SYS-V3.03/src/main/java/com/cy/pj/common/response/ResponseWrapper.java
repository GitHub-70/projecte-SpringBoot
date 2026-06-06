package com.cy.pj.common.response;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * HttpServletResponse 包装器，用于捕获和修改响应体
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ServletOutputStream servletOutputStream;
    private PrintWriter printWriter;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream == null) {
            servletOutputStream = new CachedServletOutputStream(byteArrayOutputStream);
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null) {
            printWriter = new PrintWriter(new OutputStreamWriter(
                    byteArrayOutputStream, getCharacterEncoding()));
        }
        return printWriter;
    }

    /**
     * 获取响应体内容
     */
    public String getResponseBody() {
        try {
            // 确保所有数据都已写入
            if (printWriter != null) {
                printWriter.flush();
            }
            return byteArrayOutputStream.toString(getCharacterEncoding());
        } catch (Exception e) {
            throw new RuntimeException("获取响应体失败", e);
        }
    }

    /**
     * 设置响应体内容
     */
    public void setResponseBody(String body) throws IOException {
        byte[] bytes = body.getBytes(getCharacterEncoding());

        // 清空原有内容
        byteArrayOutputStream.reset();
        byteArrayOutputStream.write(bytes);

        // 更新 Content-Length
        setContentLength(bytes.length);
    }

    /**
     * 将捕获的响应写入原始响应
     */
    public void writeToOriginalResponse(HttpServletResponse originalResponse) throws IOException {
        byte[] bytes = byteArrayOutputStream.toByteArray();
        originalResponse.setContentLength(bytes.length);
        originalResponse.getOutputStream().write(bytes);
        originalResponse.getOutputStream().flush();
    }

    /**
     * 自定义 ServletOutputStream
     */
    private static class CachedServletOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream outputStream;

        public CachedServletOutputStream(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // 不需要异步支持
        }
    }
}

