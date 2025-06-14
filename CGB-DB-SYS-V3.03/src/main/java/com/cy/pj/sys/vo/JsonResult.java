package com.cy.pj.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据表象层 VO
 * VO<--BO<--DTO<--PO
 * VO<--DTO<--PO
 * VO<--POJO<--PO
 * BO：业务对象，如：订单业务对象，如：订单业务对象中包含客户信息，商品信息，货运信息
 * 借助此对象封装控制层响应到客户端的数据,在这个对象中会为数据添加一个状态.
 *
 * 统一响应数据结构
 * 支持成功/失败状态标识
 * 支持多种构造方式
 * 支持扩展调试信息
 */
@ApiModel(value = "公共响应结果")
public class JsonResult implements Serializable {
	private static final long serialVersionUID = 5110901796917551720L;

	/** 状态码:信息标识 1表示success,0表示error */
	@ApiModelProperty(value = "状态码,1表示success,0表示error")
	private Integer state = 1;

	/** 状态码对应的信息 */
	@ApiModelProperty(value = "状态码对应的信息")
	private String message = "success";

	/** 响应数据 */
	@ApiModelProperty(value = "响应数据")
	private Object data;

	/** 错误码（可选） */
	private String errorCode;

	/** 调试信息（仅开发环境显示） */
	private Map<String, Object> debugInfo;

	// 默认构造方法
	public JsonResult() {}

	// 带消息的构造方法
	public JsonResult(String message) {
		this.message = message;
	}

	// 带数据的构造方法
	public JsonResult(Object data) {
		this.data = data;
	}

	// 异常处理构造方法
	public JsonResult(Throwable e) {
		this.state = 0;
		this.message = sanitizeErrorMessage(e.getMessage());
		this.errorCode = e.getClass().getSimpleName();

		if (isDevEnvironment()) {
			Map<String, Object> info = new HashMap<>();
			info.put("exceptionClass", e.getClass().getName());
			info.put("stackTrace", e.getStackTrace());
			this.debugInfo = info;
		}
	}

	/**
	 * 安全信息过滤
	 * @param message
	 * @return String
	 */
	private String sanitizeErrorMessage(String message) {
		if (message == null) {
			return "未知错误";
		}

		// 过滤敏感信息
		if (message.contains("password") || message.contains("密钥")) {
			return "系统错误";
		}

		return message;
	}

	/**
	 * 判断是否为开发环境
	 * @return boolean
	 */
	private boolean isDevEnvironment() {
		return "dev".equals(System.getProperty("spring.profiles.active"));
	}

	// Getters and Setters
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, Object> getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(Map<String, Object> debugInfo) {
		this.debugInfo = debugInfo;
	}
}