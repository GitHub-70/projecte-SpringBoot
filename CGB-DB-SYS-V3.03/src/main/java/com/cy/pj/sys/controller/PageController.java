package com.cy.pj.sys.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

/**
 * 	基于此Controller处理所有页面请求
 */
@RequestMapping("/")
@Controller
public class PageController {
	
	@RequestMapping("doLoginUI")
	public String doLoginUI() {
		return "login";
	}
	//http://localhost/log/log_list
	//http://localhost/menu/menu_list
	//rest风格的url,{}代表是变量表达式
	
	// 此路径的映射 可能造成一些静态资源无法解析--500错误 如：
	// http://localhost:8082/base64-js-master/base64js.min.js 
	// 无法解析 Error resolving template [sys/base64js.min.js]
	// 因此 多增加了一个文件夹 dele-sys-mapping
	@RequestMapping("{module}/{moduleUI}")
	public String doModuleUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}
	
//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
//
//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}
	

	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}

	@Autowired
	private SysMenuService sysMenuService;
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		//从shiro框架的session对象中取出登录用户信息
		SysUser user=ShiroUtils.getUser();
		//获取用户有访问权限的菜单
		List<SysUserMenu> userMenus=sysMenuService.findUserMenus(user.getId());
		System.out.println("userMenus="+userMenus);
		//将用户对象存储到model
		model.addAttribute("user", user);
		model.addAttribute("userMenus", userMenus);
		return "starter";//ThymeleafViewResolver
	}
}
