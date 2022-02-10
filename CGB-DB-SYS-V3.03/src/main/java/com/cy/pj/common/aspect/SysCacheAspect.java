package com.cy.pj.common.aspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysCacheAspect {
	 //假设此对象为存储数据的一个缓存对象
	 private Map<String,Object> cache=new ConcurrentHashMap<>();//线程安全的hashmap
	
     //注解方式的切入点表达式的定义(细粒度的表达式)
	 @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	 public void doCache() {}
	 
	 @Pointcut("@annotation(com.cy.pj.common.annotation.ClearCache)")
	 public void doClear(){}
	 
	 //????用什么通知方法去清除cache数据
	 
	 @AfterReturning("doClear()")
	 public void doClearCache() {
		 cache.clear();
		 //cache.remove("deptKey");
	 }
	 
	 @Around("doCache()")
	 public Object doAround(ProceedingJoinPoint jp)throws Throwable{
		 System.out.println("Get Data from cache");
		 Object result=cache.get("deptKey");//这里的deptKey目前为一个固定值
		 // 如果缓存不为null 直接返回
		 if(result!=null)
			 return result;
		 // 否则执行目标方法
		 result=jp.proceed();
		 cache.put("deptKey", result);
		 System.out.println("Put data to cache");
		 return result;
	 }
	 
	 
	 
}
