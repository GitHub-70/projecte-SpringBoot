package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 注意切面的执行循序
 * 		围绕目标方法，是一个一个切面圆，内层切面圆是order2 AOP,外层切面圆是order1 AOP
 */
@Aspect
@Component
@Order(1)
public class SysTimeAspect {

	@Pointcut("bean(sysUserServiceImpl)")
	public void doTime() {}
	
	/**before通知在目标方法执行之前执行*/
	@Before("doTime()")
	public void doBefore(JoinPoint jp) {
		System.out.println("@Before");
	}
	/**after通知在目标方法结束之前(return或throw之前)执行*/
	@After("doTime()")
	public void doAfter() {
		System.out.println("@After");
	}
	/**after之后程序没有出现异常则执行此通知*/
	@AfterReturning("doTime()")
	public void doAfterTurning() {
		System.out.println("@AfterReturning");
	}
	/**after之后程序出现异常则执行此通知*/
	@AfterThrowing("doTime()")
	public void AfterThrowing() {
		System.out.println("@AfterThrowing");
	}
	//ProceedingJoinPoint这个类型只能作为环绕通知的方法参数
	@Around("doTime()")
	public Object doAround(ProceedingJoinPoint jp) throws Throwable{
		System.out.println("@Around.Before");
		try {
		Object result=jp.proceed();
		System.out.println("@Around.after");
		return result;
		}catch(Throwable e) {
		System.out.println("@Around.error");
		throw e;
		}
	}
}



