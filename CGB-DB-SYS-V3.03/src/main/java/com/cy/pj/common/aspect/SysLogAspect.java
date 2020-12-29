package com.cy.pj.common.aspect;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.utils.IPUtils;
import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * @Aspect 注解描述的类为spring aop中的一个切面类型,此类型可以定义:
 * 1)切入点(PointCut)方法(可以是多个):要进行功能扩展的一些点
 * 2)通知(Advice)方法(可以是多个):封装了扩展功能的一些方法(在切入点方法之前或之后要执行的方法)
 */
//@Order(1)
@Aspect
@Component
public class SysLogAspect {
	
	
	private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);

     /**
      * @Pointcut 注解描述的方法为切入点方法,注解中定义的内容为切入点表达式(可以有多种形式)
      * 1)bean(bean名称) 切入点表达式,这个表达式中的名字为spring容器中管理的一个bean的名字
      * 2)bean表达式是一种粗粒度的切入点表达式,这种表达式定义的切入点表示bean中的所有方法
             *  都是将来要切入扩展功能的一些方法(目标方法)
             *  在当前应用中,sysUserServiceImpl这个名字对应的bean中所有的方法的集合为切入点
      */
	 //@Pointcut("bean(sysUserServiceImpl)")
	 @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	 public void doLogPointCut() {}//方法中不写任何内容,只是切入点表达式的载体
	

	 /**
	  * 	@Around 注解描述的方法为一个通知方法，这个通知我们称之为环绕通知，可以在
	  * 	目标方法执行之前或之后做服务增益。在环绕通知方法我们可以自己控制目标方法的
	  * 	调用。
	  * @param jp 连接点对象,此对象封装了要执行的目标方法信息
	  * @return 目标的执行结果
	  * @throws 执行目标方法过程中出现的异常。
	  */
	 @Around("doLogPointCut()")
	 public Object doLogAround(ProceedingJoinPoint jp)throws Throwable {
		 long t1=System.currentTimeMillis();
		 try {
		 Object result=jp.proceed();//假如本类有@Before先执行@Before,没有看是否有一个切面,最后执行你的目标方法
		 long t2=System.currentTimeMillis();
		 log.info("目标方法执行时长:{}",t2-t1);
		 //将用户行为信息写入到数据库
		 saveUserLog(jp,(t2-t1));
		 return result;//目标方法的执行结果
		 }catch(Throwable e) {
		 log.error("目标方法执行过程中出现了问题，具体为{}",e.getMessage());
		 throw e;
		 }
	 }
	 @Autowired
	 private SysLogService sysLogService;
	 private void saveUserLog(ProceedingJoinPoint jp,long time)throws Throwable {
		 //1.获取用户行为日志
		 //1.1获取ip地址
		 String ip=IPUtils.getIpAddr();
		 //1.2获取登录用户名
		 String username=ShiroUtils.getUsername();//做完登录以后再修改,暂时先用固定值
		 //1.3获取操作名
		 //1.3.1获取目标对象类型
		 Class<?> targetCls=jp.getTarget().getClass();
		 //1.3.2获取目标方法
		 MethodSignature ms=(MethodSignature)jp.getSignature();
		 String methodName=ms.getName();
		 Method targetMethod=targetCls.getMethod(methodName, ms.getParameterTypes());
		 //1.3.3获取目标方法上的注解
		 RequiredLog requiredLog=targetMethod.getAnnotation(RequiredLog.class);
		 //1.3.4获取RequiredLog注解中定义的value属性值
		 String operation="operation";
		 if(requiredLog!=null) {//假如是切入点表示是注解方式则不需要再做判断.
			 operation=requiredLog.value();
		 }
		 //1.4获取目标方法信息(类全名+方法名)
		 String method=targetCls.getName()+"."+methodName;
		 //1.5获取方法执行时传入的实际参数
		 //String params=Arrays.toString(jp.getArgs());//转普通串
		 String params=new ObjectMapper().writeValueAsString(jp.getArgs());//尽量转json格式
		 //2.封装用户行为日志
		 SysLog log=new SysLog();
		 log.setIp(ip);
		 log.setUsername(username);
		 log.setOperation(operation);
		 log.setMethod(method);//method 构成为目标方法所在类的类全名以及方法名
		 log.setParams(params);//访问方法时传递的实际参数
		 log.setTime(time);
		 log.setCreatedTime(new Date());
		 //3.存储用户行为日志
//		  new Thread() {
//			 public void run() {
//				 sysLogService.saveObject(log);
//			 };
//		  }.start(); 并发量小的时候可以这样new几个有限的线程
		 
		 sysLogService.saveObject(log);
	 }
	 


}







