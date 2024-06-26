package com.cy.pj.common.interceptor;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.utils.UserThreadLocal;
/**
 * 在spring框架所有实现了HandlerInterceptor的对象都是spring mvc中拦截器对象
 */
public class TimeAccessInterceptor implements HandlerInterceptor {
    /**此方法在@Controller描述的对象方法执行之前执行
     * @return true表示请求放行,false表示请求到此结束.
     * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("==preHandler==");
		//获取当前时间(LocalDateTime为jdk8中的一个日历对象)
		LocalDateTime localDateTime=LocalDateTime.now();
		//获取当前时间对应的小时
		int hour=localDateTime.getHour();
		System.out.println("获取当前时间hour="+hour);
		System.out.println(request.getRequestURL()+"?"+request.getQueryString());
//		if(hour<=6||hour>=23)
//			throw new ServiceException("请在9:00~18:00之间访问");
//		if(hour<=24)
//			throw new ServiceException("请在9:00~18:00之间访问");
		return true;//true表示要执行后续拦截器方法或者目标@Controller对象方法
		
	}
	/**控制层@Controller方法执行结束以后执行*/
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		System.out.println("===postHandle===");
//	}
	
	/**控制层@Controller方法响应的view解析结束以后执行，常用于资源的释放，如ThreadLocal.remove*/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 释放资源
		UserThreadLocal.remove();
		System.out.println("===AfterCompletion===");
	}
}






