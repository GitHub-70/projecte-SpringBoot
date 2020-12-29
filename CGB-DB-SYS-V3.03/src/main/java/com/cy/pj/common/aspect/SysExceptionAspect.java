package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
public class SysExceptionAspect {
	
	private static final Logger log = LoggerFactory.getLogger(SysExceptionAspect.class);

	
	/**此方法可以作为一个异常监控方法*/
	@AfterThrowing(pointcut = "bean(*ServiceImpl)",throwing = "ex")
	public void handleException(JoinPoint jp,Throwable ex) {
		//通过连接点获取目标对象类型
		Class<?> targetClass=jp.getTarget().getClass();
		String className=targetClass.getName();
		//通过连接点获取方法签名对象
		MethodSignature s=(MethodSignature)jp.getSignature();
		String methodName=s.getName();//获取目标方法名
		String targetClassMethod=className+"."+methodName;
		log.error("{}'exception msg is  {}",targetClassMethod,ex.getMessage());
		
		//拓展?
		//1)将日志写到日志文件
		//2)将出现异常的这个信息发送到某个人的邮箱(email),例如QQ邮箱
		//3)将出现异常的情况发短信给某人(运维人员)
		//4)报警(播放一段难听的音乐)
	}
	
}





