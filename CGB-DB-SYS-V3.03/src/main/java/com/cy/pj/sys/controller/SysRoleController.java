package com.cy.pj.sys.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cy.pj.sys.vo.JsonResult;
import com.cy.pj.sys.po.SysRole;
import com.cy.pj.sys.service.SysRoleService;

@Api(tags = "角色模块")
@RestController
@RequestMapping("/role/")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

	@ApiOperation(value = "查询角色信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindRoles")
	public JsonResult doFindRoles() {
		return new JsonResult(sysRoleService.findRoles());
	}

	@ApiOperation(value = "根据id查询角色信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "角色id", required = true, dataType = "Integer")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(sysRoleService.findObjectById(id));
	}

	@ApiOperation(value = "修改角色信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "menuIds", value = "菜单id数组", required = true, dataType = "Integer[]")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@PostMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysRole entity,Integer[]menuIds) {
		sysRoleService.updateObject(entity, menuIds);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "保存角色信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "menuIds", value = "菜单id数组", required = true, dataType = "Integer[]")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "保存成功"),
			@ApiResponse(code = 0, message = "保存失败")
	})
	@PostMapping("doSaveObject")
	public JsonResult doSaveObject(SysRole entity,Integer[]menuIds) {
		sysRoleService.saveObject(entity, menuIds);
		return new JsonResult("save ok");
	}

	@ApiOperation(value = "分页查询角色信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "角色名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "pageCurrent", value = "当前页", required = true, dataType = "Long")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String name,Long pageCurrent) {
		return new JsonResult(sysRoleService.findPageObjects(name, pageCurrent));
	}
}




