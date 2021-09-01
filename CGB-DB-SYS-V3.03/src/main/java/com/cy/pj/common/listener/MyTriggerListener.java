package com.cy.pj.common.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyTriggerListener implements TriggerListener{

	private static final Logger logger = LoggerFactory.getLogger(MyTriggerListener.class);
	/**
	 * 自定义 trigger 监听器名称
	 */
	@Override
	public String getName() {
		
		return "myTriggerListener";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		logger.info("********trigger监听器：[{}],对[{}]进行监听，设置合适的Missfire策略********", getName(), trigger.getKey());
		logger.info(" Trigger 被触发了，此时Job 上的 execute() 方法将要被执行");
		
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		logger.info("发现此次Job的相关资源准备存在问题，不便展开任务，返回true表示 否决此次任务执行");
        return false;
//        return true;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		logger.info( "当前Trigger触发错过了");
		
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		logger.info("Trigger 被触发并且完成了 Job 的执行,此方法被调用");
		
	}

}
