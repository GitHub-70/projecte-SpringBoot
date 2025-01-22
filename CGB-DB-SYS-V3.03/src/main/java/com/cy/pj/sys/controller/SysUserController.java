package com.cy.pj.sys.controller;


import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.po.SysUser;
import com.cy.pj.sys.service.SysUserService;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user/")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@ApiOperation(value = "修改密码",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pwd", value = "旧密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "newPwd", value = "新密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "cfgPwd", value = "确认密码", required = true, dataType = "String")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "修改密码成功"),
			@ApiResponse(code = 500, message = "修改密码失败"),
			@ApiResponse(code = 400, message = "参数错误"),
			@ApiResponse(code = 401, message = "用户未登录"),
			@ApiResponse(code = 402, message = "用户名或密码错误"),
			@ApiResponse(code = 403, message = "用户无权限"),
			@ApiResponse(code = 404, message = "资源不存在"),
			@ApiResponse(code = 405, message = "请求方法错误")
	})
	@PostMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(String pwd,String newPwd,String cfgPwd) {
		sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "用户登录",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "isRememberMe", value = "是否记住我", required = true, dataType = "boolean")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "登录成功"),
			@ApiResponse(code = 0, message = "登录失败")
	})
	@PostMapping("doLogin")
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

	@ApiOperation(value = "根据id查询用户信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id", required = true)
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(sysUserService.findObjectById(id));
	}

	@ApiOperation(value = "修改用户信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleIds", value = "角色id数组", required = true, dataType = "Integer[]")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@PostMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,Integer[]roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "保存用户信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "roleIds", value = "角色id数组", required = true, dataType = "Integer[]")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "保存成功"),
			@ApiResponse(code = 0, message = "保存失败")
	})
	@PostMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,Integer[]roleIds) throws Exception {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}

	@ApiOperation(value = "修改用户状态",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "valid", value = "用户状态", required = true, dataType = "Integer")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@PostMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid){
		sysUserService.validById(id, valid);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "查询用户信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "pageCurrent", value = "当前页", required = true, dataType = "Long")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Long pageCurrent) {
		return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	}
	
}
