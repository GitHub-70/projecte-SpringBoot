package com.cy.pj.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.common.filter.MyFilter;

//@Configuration
public class FilterConfig {

	//定义过滤路径
	private List<String> getFilterUrls() {
		List<String> holdleUrls = new ArrayList<String>();
		holdleUrls.add("/*");
		return holdleUrls;
		}

	@Bean
	public FilterRegistrationBean<MyFilter> filterRegistrationBean() {
		FilterRegistrationBean<MyFilter> reg = new FilterRegistrationBean<MyFilter>();
		reg.setFilter(new MyFilter());//添加过滤器
		reg.setUrlPatterns(getFilterUrls());//设置过滤路径
		reg.setName("myLoginFilter");//设置过滤器名称
		reg.setOrder(1);//设置过滤器顺序,数字大于0时越小,优先级越高
		return reg;
		}
}