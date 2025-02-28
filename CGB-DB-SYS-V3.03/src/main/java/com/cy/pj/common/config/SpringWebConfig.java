package com.cy.pj.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cy.pj.common.interceptor.TimeAccessInterceptor;
/***
 * Spring MVC 中的配置对象(此对象会在spring容器启动时初始化)
 *
 * 	WebMvcConfigurer 是一个接口，用于自定义 Spring MVC 的配置。它提供了一些方法，允许开发者自定义 MVC 配置，例如：
 * 		添加自定义的视图解析器
 * 		配置静态资源的处理
 * 		定义自定义的拦截器
 * 		配置 MVC 的其他设置
 */
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

	/**注册拦截器对象*/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册拦截器
		registry.addInterceptor(new TimeAccessInterceptor())
		//设置要拦截的路径:/user/doLogin
//		.addPathPatterns("/user/doLogin");
		.addPathPatterns("*");
	}
}
