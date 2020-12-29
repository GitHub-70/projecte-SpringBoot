package com.cy.pj.sys.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 	基于此Controller处理所有页面请求
 */
@RequestMapping("/")
@RestController
public class PortController {
	
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("getPort")
	public String getPort() {
		return "当前端口为："+port;
	}
	
}
