package com.cy.pj.common.exception;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiResponse;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cy.pj.sys.vo.JsonResult;


/**
 *	 由此注解描述的类为一个控制层全局异常处理类,在此类中可以定义异常处理方法
 *,基于这些异常处理方法对异常进行处理.
 *
 * 在全局异常处理器中，这些异常处理方法的匹配顺序应遵循：
 * ConstraintViolationException → MethodArgumentNotValidException →
 * BindException → MissingServletRequestParameterException →
 * IllegalArgumentException → Exception
 *
 */
//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice //==@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {

	/**
	 *
	 */
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@ExceptionHandler(ShiroException.class) 
	//@ResponseBody
	public JsonResult doHandleShiroException(ShiroException e) {
		JsonResult result = new JsonResult();
		result.setState(0);

		String message;
		if (e instanceof UnknownAccountException) {
			message = "账户不存在";
		} else if (e instanceof LockedAccountException) {
			message = "账户已被禁用";
		} else if (e instanceof IncorrectCredentialsException) {
			message = "密码不正确";
		} else if (e instanceof AuthorizationException) {
			message = "没有此操作权限";
		} else {
			message = "系统维护中";
		}

		result.setMessage(message);
		//  记录异常信息
		logErrorDetails(e, message);
		return result;
	}

	/**
	 * 验证异常统一处理
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler({
			ConstraintViolationException.class,
			MethodArgumentNotValidException.class,
			BindException.class,
			MissingServletRequestParameterException.class
	})
	public JsonResult handleValidationExceptions(Exception e, HttpServletRequest request) {
		String errorMessage = "参数验证失败: " + e.getMessage();
		try {
			String requestParams = getRequestParams(request);
			logger.warn("Validation error [{}]: {} | Params: {}",
					e.getClass().getSimpleName(), errorMessage, requestParams);

			JsonResult result = new JsonResult();
			result.setState(0);
			result.setMessage(errorMessage);
			result.setData(Collections.singletonMap("invalidParams", requestParams));
			return result;
		} catch (Exception ex) {
			logger.error("处理验证异常时发生错误", ex);
			return new JsonResult("参数处理异常");
		}
	}

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
//		String errorMessage = ex.getBindingResult()
//				.getAllErrors()
//				.stream()
//				.map(DefaultMessageSourceResolvable::getDefaultMessage)
//				.collect(Collectors.joining(", "));
//
//		return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
//	}

	// 运行时异常处理
	@ExceptionHandler(RuntimeException.class)
	public JsonResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
		String errorMessage = "运行时异常: " + e.getMessage();
		logger.error("Runtime error [{}]: {} | Request: {}",
				e.getClass().getSimpleName(), errorMessage, getRequestDetails(request), e);

		JsonResult result = new JsonResult();
		result.setState(0);
		result.setMessage(isDevEnvironment() ? errorMessage : "系统内部错误");
		result.setData(Collections.singletonMap("errorClass", e.getClass().getName()));
		return result;
	}

	/**
	 * @ExceptionHandler 此注解描述的方法为一个异常处理方法,在注解内部定义的异常
	 *  类型为此方法可以处理的异常类型(包括异常的子类类型).
	 * @param e 用于接收出现的异常
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	//@ResponseBody
	public JsonResult handleServiceException(ServiceException e, HttpServletRequest request) {
		String errorMessage = "自定义服务错误: " + e.getMessage();
		logger.error("My Service error [{}]: {} | Request: {}",
				e.getClass().getSimpleName(), errorMessage, getRequestDetails(request), e);

		JsonResult result = new JsonResult();
		result.setState(0);
		result.setMessage(errorMessage);
		result.setData(Collections.singletonMap("errorClass", e.getClass().getName()));
		return result;
	}

	// 空指针异常处理
	@ExceptionHandler(NullPointerException.class)
	public JsonResult handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		String errorMessage = "空指针异常: " + e.getMessage();
		logger.error("NPE error: {} | Request: {}", errorMessage, getRequestDetails(request), e);

		JsonResult result = new JsonResult();
		result.setState(0);
		result.setMessage(isDevEnvironment() ? errorMessage : "系统内部错误");
		return result;
	}

	// HTTP方法不支持处理
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public JsonResult handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		String errorMessage = "HTTP方法不支持: " + e.getMethod();
		logger.warn("HTTP method not supported: {} | Supported: {} | Request: {}",
				e.getMethod(), e.getSupportedMethods(), getRequestDetails(request));

		JsonResult result = new JsonResult();
		result.setState(0);
		result.setMessage(errorMessage);
		result.setData(Collections.singletonMap("supportedMethods", e.getSupportedMethods()));
		return result;
	}

	/**
	 * 通用异常处理
	 * @param e
	 * @param request
	 * @return JsonResult
	 */
	@ExceptionHandler(Exception.class)
	public JsonResult handleAllExceptions(Exception e, HttpServletRequest request) {
		String errorMessage = "未知异常: " + e.getMessage();
		logger.error("Unexpected error [{}]: {} | Request: {}",
				e.getClass().getSimpleName(), errorMessage, getRequestDetails(request), e);

		JsonResult result = new JsonResult();
		result.setState(0);
		result.setMessage(isDevEnvironment() ? errorMessage : "系统内部错误");
		result.setData(Collections.singletonMap("exceptionType", e.getClass().getName()));
		return result;
	}

	/**
	 * 记录异常信息
	 * @param e
	 * @param message
	 */
	private void logErrorDetails(Exception e, String message) {
		if (isDevEnvironment()) {
			e.printStackTrace();
		}
		logger.error("异常详情: {} | 异常类型: {}", message, e.getClass().getName());
	}

	private String getRequestParams(HttpServletRequest request) {
		try {
			return request.getParameterMap().entrySet().stream()
					.map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
					.collect(Collectors.joining("&"));
		} catch (Exception ex) {
			return "无法获取参数";
		}
	}

	/**
	 * 获取请求详细信息
	 * @param request
	 * @return String
	 */
	private String getRequestDetails(HttpServletRequest request) {
		try {
			Map<String, Object> requestDetails = new HashMap<>();
			requestDetails.put("method", request.getMethod());
			requestDetails.put("uri", request.getRequestURI());
			requestDetails.put("remoteAddr", getClientIP(request));

			// 处理请求头
			Map<String, String> headersMap = Collections.list(request.getHeaderNames())
					.stream()
					.collect(Collectors.toMap(
							h -> h,
							h -> request.getHeader(h)
					));
			requestDetails.put("headers", headersMap);

			return objectMapper.writeValueAsString(requestDetails);
		} catch (JsonProcessingException e) {
			logger.warn("请求信息序列化失败", e);
			return "请求信息序列化失败";
		}
	}


	/**
	 * 获取客户端IP
	 * @param request
	 * @return String
	 */
	private String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 判断是否为开发环境
	 * @return
	 */
	private boolean isDevEnvironment() {
		return "dev".equals(System.getProperty("spring.profiles.active"));
	}

}






