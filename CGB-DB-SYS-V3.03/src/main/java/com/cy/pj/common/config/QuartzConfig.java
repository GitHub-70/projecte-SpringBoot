package com.cy.pj.common.config;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ListenerManager;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.common.listener.MyJobListener;
import com.cy.pj.common.listener.MyTriggerListener;
import com.cy.pj.common.schedul.MyPrintWritejob;

/**
 * todo 如何将多个 Trigger 触发器 配置到一个jobDetail中
 * @author Administrator
 *
 */


@Configuration
public class QuartzConfig {
	
	@Autowired
	private MyJobListener myJobListener;
	@Autowired
	private MyTriggerListener myTriggerListener;
	
	/**
	 * job任务
	 * @return
	 */
	@Bean
	public JobDetail jobDetail() {
		JobDetail jobDetail = JobBuilder.newJob(MyPrintWritejob.class)
		.usingJobData("dataKey_1","这个Job用来测试的")
		.withIdentity("dataKey_1")
		// 在将JobDetails信息持久化到数据库的时候有一个属性storeDurably，
		// 如果设置为true则无论与其关联的Trigger是否存在其都会一直存在，
		// 否则只要相关联的trigger删除掉了其会自动删除掉,默认值为true
//		.storeDurably()
		.build();
		// 该任务是否并发执行
//		jobDetail.isConcurrentExectionDisallowed();
		return jobDetail;
	}
	
	/**
	 * 配置触发器 Trigger
	 * @return
	 */
	@Bean
	public CronTrigger cronTrigger1(JobDetail jobDetail) {
		
		 // 构建Trigger实例
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);

		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("TriggerKey_1", "triggerGroup1")// 触发器的名称，组名
				.usingJobData("TriggerKey_1", "这是jobDetail1的trigger")
				.startNow()// 立即生效
				.startAt(startDate) // * 30 10 ? * 1/5 2021
				.withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))// 每10秒执行一次
//				.forJob(jobDetail)// 一个触发器 只能绑定到一个jobDetail中
				.build();
		return cronTrigger;
	}
	
	/**
	 * 配置触发器 Trigger
	 * @return
	 */
	@Bean
	public CronTrigger cronTrigger2(JobDetail jobDetail) {
		
		 // 构建Trigger实例
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);

		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("TriggerKey_2", "triggerGroup1")// 触发器的名称，组名
				.usingJobData("TriggerKey_2", "这是jobDetail1的trigger")
				.startNow()// 立即生效
				.startAt(startDate) // * 30 10 ? * 1/5 2021
				.withSchedule(CronScheduleBuilder.cronSchedule("0/40 * * * * ?"))// 每40秒执行一次
//				.forJob(jobDetail)// 一个触发器 只能绑定到一个jobDetail中
				.build();
		return cronTrigger;
	}
	
	/**
	 * 任务调度器
	 * @return
	 * @throws SchedulerException
	 * 
	 * 用@Qualifier 可以指定某一cronTrigger
	 */
	@Bean
	public Scheduler scheduler(JobDetail jobDetail, @Qualifier("cronTrigger1")CronTrigger cronTrigger) throws SchedulerException {
		SchedulerFactory  schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		// 获取监听管理器，并将 job监听器 注册进去（全局注册）
		ListenerManager listenerManager = scheduler.getListenerManager();
		listenerManager.addJobListener(myJobListener);
		// 注册 trigger 监听器
		listenerManager.addTriggerListener(myTriggerListener);
		// 绑定 jobDetail 与 cronTrigger---方式一
		scheduler.scheduleJob(jobDetail, cronTrigger);
		// 绑定 jobDetail 与 cronTrigger---方式二
//		scheduler.addJob(jobDetail, true);
//		scheduler.scheduleJob(cronTrigger);// cronTrigger1
//		scheduler.scheduleJob(cronTrigger);// cronTrigger2
		scheduler.start();
		return scheduler;
	}
}
