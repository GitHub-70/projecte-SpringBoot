package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class SysTimeAspect2 {

	/**
	 * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?
	 * 	name-pattern(param-pattern) throws-pattern?)
	 * 	modifiers-pattern表示方法的访问类型，public等；
	 * 	ret-type-pattern表示方法的返回值类型，如String表示返回类型是String，“*”表示所有的返回类型；
	 * 	declaring-type-pattern表示方法的声明类，如“com.elim..*”表示com.elim包及其子包下面的所有类型；
	 * 	name-pattern表示方法的名称，如“add*”表示所有以add开头的方法名；param-pattern表示方法参数的类型，
	 * 		name-pattern(param-pattern)其实是一起的表示的方法集对应的参数类型，
	 * 		如“add()”表示不带参数的add方法，“add(*)”表示带一个任意类型的参数的add方法，
	 * 		“add(*,String)”则表示带两个参数，且第二个参数是String类型的add方法；
	 * 	throws-pattern表示异常类型；其中以问号结束的部分都是可以省略的。
	 */
	@Pointcut("this(com.cy.pj.sys.service.impl.SysUserServiceImpl)")
	public void doTime() {}
	
	/**before通知在目标方法执行之前执行*/
	@Before("doTime()")
	public void doBefore(JoinPoint jp) {
		System.out.println("@Before-2");
	}
	/**after通知在目标方法结束之前(return或throw之前)执行*/
	@After("doTime()")
	public void doAfter() {
		System.out.println("@After-2");
	}
	/**after之后程序没有出现异常则执行此通知*/
	@AfterReturning("doTime()")
	public void doAfterTurning() {
		System.out.println("@AfterReturning-2");
	}
	/**after之后程序出现异常则执行此通知*/
	@AfterThrowing("doTime()")
	public void AfterThrowing() {
		System.out.println("@AfterThrowing-2");
	}
	//ProceedingJoinPoint这个类型只能作为环绕通知的方法参数
	@Around("doTime()")
	public Object doAround(ProceedingJoinPoint jp) throws Throwable{
		System.out.println("@Around.Before-2");
		try {
		Object result=jp.proceed();
		System.out.println("@Around.after-2");
		return result;
		}catch(Throwable e) {
		System.out.println("@Around.error-2");
		throw e;
		}
	}
}



