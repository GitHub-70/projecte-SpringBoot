package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author Administrator
 * 使用@EnableAsync 需自定义线程池，
 * 因@EnableAsync默认使用的线程池是
 * SimpleAsyncTaskExecutor，而SimpleAsyncTaskExecutor 不是真的线程池，
 * 这个类不重用线程，每次调用都会创建一个新的线程。并发大的时候会产生严重的性能问题。
 * 
 * https://blog.csdn.net/suixinfeixiangfei/article/details/123398420
 *
 * 项目启动失败：The method's class, com.google.common.collect.FluentIterable, is available from the following locations:
 *     jar:file:/E:/repository/idea_repo/springboot_repo/com/google/guava/guava/18.0/guava-18.0.jar!/com/google/common/collect/FluentIterable.class
 *     发现可能使用 guava 版本冲突原因，在swagger2.0.0版本中引入了guava依赖，导致版本冲突
 *
 */

//@EnableScheduling // 启动定时任务
@EnableCaching
@EnableAsync //启动异步操作,底层会初始化一个线程池
@SpringBootApplication
//可使用AopContext.currentProxy() 生成独立的动态代理对象，常用于spring代理对象的事务管理
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// SpringBoot项目打war包
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		
//		return builder.sources(Application.class);
//	}
	
}
