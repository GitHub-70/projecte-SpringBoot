package com.cy.pj.common.config;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.common.schedul.MyPrintWritejob;


@Configuration
public class QuartzConfig {
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public JobDetail jobDetail() {
		JobDetail jobDetail = JobBuilder.newJob(MyPrintWritejob.class)
		.usingJobData("dataKey_1","这个Job用来测试的")
		.withIdentity("dataKey_1")
		.build();
		return jobDetail;
	}
	
	/**
	 * 配置触发器 Trigger
	 * @return
	 */
	@Bean
	public CronTrigger cronTrigger() {
		
		 // 构建Trigger实例
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);

		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("TriggerKey_1", "triggerGroup1")
				.usingJobData("TriggerKey_1", "这是jobDetail1的trigger")
				.startNow()// 立即生效
				.startAt(startDate) // * 30 10 ? * 1/5 2021
				.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))// 每10秒执行一次
				.build();
		return cronTrigger;
	}
	/**
	 * 任务调度器
	 * @return
	 * @throws SchedulerException
	 */
	@Bean
	public Scheduler scheduler(JobDetail jobDetail, CronTrigger cronTrigger) throws SchedulerException {
		SchedulerFactory  schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.scheduleJob(jobDetail, cronTrigger);
		scheduler.start();
		return scheduler;
	}
}
