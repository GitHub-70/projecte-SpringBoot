package com.cy.pj.sys.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.common.utils.UserThreadLocal;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.service.impl.SysUserServiceImpl;

@RestController
@RequestMapping("/user/")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(String pwd,String newPwd,String cfgPwd) {
		sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doLogin")
	public JsonResult doLogin(String username,String password,
			boolean isRememberMe) {
		System.out.println("==doLogin===");
		//获取Subject对象(负责提交客户端的账号信息)
		Subject subject=SecurityUtils.getSubject();
		// 设置登录超时时间15分钟 默认超时时间为30分钟  不超时为负数
		subject.getSession().setTimeout(900000);
		UsernamePasswordToken token=new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		//提交账号信息给securityManager对象
		token.setRememberMe(isRememberMe);//设置记住我
		// 将用户信息 交给一个 独立线程
//		UserThreadLocal.set(token);
		subject.login(token);
		return new JsonResult("login ok");
	}
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(sysUserService.findObjectById(id));
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,Integer[]roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,Integer[]roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid){
		sysUserService.validById(id, valid);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Long pageCurrent) {
		return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	}
	
}
