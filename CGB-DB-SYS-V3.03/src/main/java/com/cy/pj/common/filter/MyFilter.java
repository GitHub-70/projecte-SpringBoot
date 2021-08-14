package com.cy.pj.common.filter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter implements Filter{
	
	// 请求服务器网站资源量
	private  AtomicInteger count;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化工作
		count = new AtomicInteger(0);
		System.out.println("--------count="+count+"---------");
		
		//获配置文件中的init-param初始化参数 即web.xml中init-param
//		String param = filterConfig.getInitParameter("count");
		//将字符串转换为int
//		count = Integer.valueOf(param);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		System.out.println("-------Myfilter-------");
		// count自增
		System.out.println("--------count="+count+"---------");
		count.incrementAndGet();
		ServletContext servletContext = req.getServletContext();
		//将 访问服务器数量值 放入到web服务器Servlet上下文ServletContext中
		servletContext.setAttribute("count", count);
		String requestURI = req.getRequestURI();
		StringBuffer requestURL = req.getRequestURL();
		// 请求url及请求参数
		System.out.println("requestURI-->"+requestURI+"?"+servletContext.getAttribute("count"));
		
		System.out.println("requestURL-->"+requestURI+"?"+servletContext.getAttribute("count"));
		// 放行，转到下一个过滤器
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		// Filter容器销毁前，释放资源
		
	}

}
