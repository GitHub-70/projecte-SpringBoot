package com.cy.pj.common.schedul;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 定时任务类
 * @author Administrator
 *
 */
@Component
public class MyEveryDayjob implements Job{
	
	private Logger logger = LoggerFactory.getLogger(MyEveryDayjob.class);
	/**
	 * 定时输出服务器访问量(包括静态资源的访问)
	 * @param req
	 */
	public void execute(HttpServletRequest req) {
		logger.info("===进入定时任务Myjob===");
		AtomicInteger count = (AtomicInteger)req.getAttribute("count");
		if (null != count) {
			logger.info("------->该服务器的访问数量：{}",count);
		}
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("===进入定时任务Myjob===");
		JobDataMap jobDetailDataMap = context.getJobDetail().getJobDataMap();
		JobDataMap triggerDataMap = context.getTrigger().getJobDataMap();
		String jobInstance = context.getJobInstance().toString();
		//
		logger.info("===通过JobExecutionContext获取MyPrintWritejob上下文[{}]信息",jobDetailDataMap.get("dataKey_2"));
		logger.info("===通过JobExecutionContext获取MyPrintWritejob上下文[{}]信息",triggerDataMap.get("TriggerKey_2"));
		logger.info("===通过JobExecutionContext获取MyPrintWritejob上下文,上一次执行时间[{}]",context.getPreviousFireTime());
		logger.info("===通过JobExecutionContext获取MyPrintWritejob上下文,下一次执行时间[{}]",context.getNextFireTime());
		logger.info("===通过JobExecutionContext获取MyPrintWritejob上下文,Jon实例[{}]",jobInstance);
		logger.info("===定时任务Myjob执行结束===");
	}

}
