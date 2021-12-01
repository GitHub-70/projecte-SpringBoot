package com.cy.pj.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

@Component
public class MyCommandLineRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

	@Override
	public void run(String... args) throws Exception {
		logger.info("==========MyCommandLineRunner  init Data start===========");
		// 利用Executors 创建一个固定线程池大小；
		ExecutorService ExecutorService = Executors.newFixedThreadPool(4);
		List<Future> futureList = new ArrayList<>();

		logger.info("当前操作系统：{}", System.getProperty("os.name"));
		logger.info("当前项目目录位置：{}", System.getProperty("user.dir"));

		File configFile = new File(System.getProperty("user.dir") + "/src/main/resources");

		// 如果该file不存在，或者 它不是个目录的话，直接返回
		if (!configFile.exists() || !configFile.isDirectory()) {
			return;
		}

		// 获取所有文件的名字
		String[] fileNameList = configFile.list();
		// 判断该目录下是否存在文件，没有文件直接返回
		if (null == fileNameList || fileNameList.length == 0) {
			return;
		}

		for (String fileName : fileNameList) {
			// 判断是否是 以application开头，以 .yml 或 .properties结尾

			// 当前文件不是以 application 开头，跳出当前循环
			if (!fileName.startsWith("application")) {
				continue;
			}

			if (fileName.endsWith(".yml")) {
				// 添加到任务中
			} else if (fileName.endsWith(".application")) {
				// 添加到任务中
			}
		}

		// 任务结束，让线程池处于shutdown状态，不在接受新的任务
		while (true) {
			// 判断任务是否完成，完成则任务量减一
			int threadNum = futureList.size();
			for (Future future : futureList) {
				if (future.isDone()) {
					threadNum--;
				}
			}
			
			// 关闭线程通道
			if (threadNum == 0) {
				ExecutorService.shutdown();
				return;
			}
		
		}
//		logger.info("==========MyCommandLineRunner  init Data end===========");
	}
}
