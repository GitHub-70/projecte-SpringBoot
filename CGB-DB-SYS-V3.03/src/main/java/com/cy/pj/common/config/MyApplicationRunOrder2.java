package com.cy.pj.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 该类用来项目启动时，初始化一些数据
 * 		如：读取配置文件，初始化数据库等
 * @author Administrator
 *
 * 实现CommandLineRunner也可以，只是run方法中的参数不同
 * 
 * 运行在监听器之前
 * 		监听器-->过滤器-->拦截器
 */

@Configuration
@Order(2) // 执行顺序
public class MyApplicationRunOrder2 implements ApplicationRunner{

	private static final Logger logger = LoggerFactory.getLogger(MyApplicationRunOrder2.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("==========MyApplicationRunOrder2  init Data start===========");
		logger.info("==========MyApplicationRunOrder2  init Data end===========");
		
	}

}
