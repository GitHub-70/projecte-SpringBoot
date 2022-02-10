package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

//@EnableScheduling // 启动定时任务
@EnableCaching
@EnableAsync //启动异步操作,底层会初始化一个线程池
@SpringBootApplication
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
