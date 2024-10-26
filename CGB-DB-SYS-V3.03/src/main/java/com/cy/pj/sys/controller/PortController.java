package com.cy.pj.sys.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 	基于此Controller处理所有页面请求
 */
@RequestMapping("/port/test/")
@RestController
public class PortController {
	
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("getPort")
	public String getPort() {
		return "当前端口为："+port;
	}

	@RequestMapping("testAopOrder1")
	public String testAopOrder1() {
		System.out.println(testAopOrder("testAopOrder1", 1));
		return "当前端口为："+port;
	}

	/**
	 * 切面执行同心圆
	 * ①Spring4.0
	 * 　　　　正常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===
	 * 				  目标方法执行===
	 * 				  切面2环绕返回===切面2环绕最终===切面2@After===切面2@AfterReturning===
	 * 				  切面1环绕返回===切面1环绕最终===切面1@After===切面1@AfterThrowing
	 * 　　　　异常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===
	 * 				  目标方法执行===
	 * 				  切面2环绕异常===切面2环绕最终===切面2@After===切面2@AfteThrowing===
	 * 				  切面1环绕异常===切面1环绕最终===切面1@After===切面1@AfterThrowing
	 *
	 * 	②Spring5.28
	 * 		正常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===
	 * 				目标方法执行===
	 * 				切面2环绕返回===切面2环绕最终===切面2@After===切面2@AfterReturning===
	 * 				切面1环绕返回===切面1环绕最终===切面1@After===切面1@AfterThrowing
	 * 		异常情况：切面1环绕前置===切面1@Before===切面2环绕前置===切面2@Before===
	 * 				目标方法执行===
	 * 				切面2@AfterThrowing===切面2@After===切面2环绕异常===切面2环绕最终===
	 * 				切面1@AfterThrowing===切面1@After===切面1环绕异常===切面1环绕最终
	 * @return
	 */
	@RequestMapping("testAopOrder2")
	public String testAopOrder2() {
		System.out.println("PortController的testAopOrder2方法...");
		System.out.println(testAopOrder("testAopOrder2", 0));
		return "当前端口为："+port;
	}


	public String testAopOrder(String method, int normalFlag){
		try {
			if (normalFlag == 1){
				System.out.println(method +":test.....");
			} else {
				System.out.println(method +":throw Exception.....");
				throw new RuntimeException("自定义异常");
			}
		} catch (Exception e) {
			System.out.println(method +":catch Exception.....");
			throw e;
		} finally {
			System.out.println(method +":finally.....");
		}
		return method+"执行完毕返回值";
	}

}
