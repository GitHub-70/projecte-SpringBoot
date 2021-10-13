package com.cy.pj.common.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MyJobListener implements JobListener{

	private Logger logger = LoggerFactory.getLogger(MyJobListener.class);
	
	/**
	 * 自定义job监听器名称
	 */
	@Override
	public String getName() {
		
		return "myJobListener";
	}

	/**
	 * 任务执行之前执行
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("********job监听器:[{}],开始对[{}]执行监听工作*********", getName(), context.getJobDetail().getJobClass());
		logger.info("这里可以完成众多任务前(公共任务)的一些资源准备工作或日志记录");
		
	}

	/**
	 * 
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("[{}]被否决执行了，可以做些日志记录。", context.getJobDetail().getJobClass());
		
	}

	/**
	 * 任务执行之后执行
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		logger.info("job监听器:[{}],对[{}]善后处理工作", getName(), context.getJobDetail().getJobClass());
		logger.info("这里可以完成众多任务后(公共任务)进行资源销毁工作或做一些任务状态处理工作");
		logger.info("********job监听器:[{}]处理结束*********", getName());
	}

}
