package com.cy.pj.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 该类用来项目启动时，初始化一些数据
 * 		如：读取配置文件，初始化数据库等
 * @author Administrator
 * 
 * 实现CommandLineRunner也可以，只是run方法中的参数不同
 */

@Component
@Order(1) // 执行顺序
public class MyApplicationRunOrder1 implements ApplicationRunner{

	private static final Logger logger = LoggerFactory.getLogger(MyApplicationRunOrder1.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("==========MyApplicationRunOrder1  init Data===========");
		
	}

}