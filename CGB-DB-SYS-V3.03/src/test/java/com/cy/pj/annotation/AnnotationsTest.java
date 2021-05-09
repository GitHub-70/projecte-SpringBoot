package com.cy.pj.annotation;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class AnnotationsTest {
	
	public static void main(String[] args) {
		
//		applicationLoadOrderTest1();
		
		applicationLoadOrderTest2();
		
//		applicationLoadOrderTest3();
	}
	
	/**
	 * spring容器先创建Job对象，在创建Job对象时，
	 * 其User对象还未被创建,User对象注入失败
	 * Job中的 autowiredTest2 方法不会被执行
	 */
	private static void applicationLoadOrderTest1() {
		// 在初始化Spring容器时，此时Job对象 已被创建
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Job.class);
		// User对象注册到Spring容器中，并不会立马创建User对象
		annotationConfigApplicationContext.register(User.class);
		// 在使用User对象时，创建User对象----可注释该方法查看
		annotationConfigApplicationContext.getBean("user");
	}
	
	/**
	 * spring容器先创建User对象，再创建Job对象时，
	 * Job中的 autowiredTest2 方法才会被执行，其
	 * User对象才会被注入，否则 autowiredTest2 不会被执行
	 */
	private static void applicationLoadOrderTest2() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(User.class);
		annotationConfigApplicationContext.register(Job.class);
		annotationConfigApplicationContext.getBean("job");
	}
	/**
	 * 初始化Spring容器失败
	 */
	private static void applicationLoadOrderTest3() {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(User.class);
		annotationConfigApplicationContext.register(Job.class);
//		annotationConfigApplicationContext.getBean("job");
	}

}

@Service	
class User{
	
	/**
	 * @PostConstruct 此注解为JDK的注解
	 * 	该注解用来描述 非静态的void（）方法,该注解描述的方法
	 * 	在构造函数之后执行，init（）方法之前执行
	 * 	该注解的方法在整个Bean初始化中的执行顺序：
	 * 		Constructor(构造方法) -> @Autowired(依赖注入) -> 
	 * 		@PostConstruct(注释的方法)
	 * 应用：在静态方法中调用依赖注入的Bean中的方法。
	 * https://blog.csdn.net/qq360694660/article/details/82877222
	 */
	@PostConstruct
	public void postConstructTest() {
		System.out.println("User对象中的--postConstructTest--被执行了");
	}
	
	public User() {
		System.out.println("--User对象创建了--");
	}
	
	

}

@Component
class Job{
	
	@PostConstruct
	public void postConstructTest() {
		System.out.println("Job对象中的--postConstructTest--被执行了");
	}
	
	/**
	 * @Autowired 描述方法
	 * 		该方法中，可做一些初始化操作
	 * @Autowired(required = false)
	 * 		表示忽略当前要注入的bean，如果有直接注入，没有跳过，不会报错。
	 * 
	 */
	@Autowired(required = false)
	public void autowiredTest1() {
		System.out.println("@Autowired--注解的方法-autowiredTest1-执行了");
	}
	
	
	@Autowired(required = false)
	public void autowiredTest2(User user) {
		System.out.println("@Autowired--注解的方法-autowiredTest2-执行了");
		if(null != user) {
			System.out.println("@Autowired--注解的方法-参数User对象被注入了---"+user);
		}
	}
	
	public Job() {
		System.out.println("--Job对象创建了--");
	}
}

