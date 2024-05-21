package com.cy.pj.sys.controller;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

//@Controller
//@ResponseBody
@Api(tags = "菜单模块")
@RestController  //@Controller+@ResponseBody
@RequestMapping("/menu/")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	@ApiOperation(value = "修改菜单信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "entity", value = "菜单信息", required = true, dataType = "SysMenu")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysMenu entity) {
		sysMenuService.updateObject(entity);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "保存菜单信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "entity", value = "菜单信息", required = true, dataType = "SysMenu")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "保存成功"),
			@ApiResponse(code = 0, message = "保存失败")
	})
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysMenu entity) {
		sysMenuService.saveObject(entity);
		return new JsonResult("save ok");
	}

	@ApiOperation(value = "查询菜单信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@RequestMapping("doFindZtreeMenuNodes")
	public JsonResult doFindZtreeMenuNodes() {
		return new JsonResult(sysMenuService.findZtreeMenuNodes());
	}

	@ApiOperation(value = "删除菜单信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "Integer")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "删除成功"),
			@ApiResponse(code = 0, message = "删除失败")
	})
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysMenuService.deleteObject(id);
		return new JsonResult("delete ok");
	}

	@ApiOperation(value = "查询菜单信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@RequestMapping("doFindObjects")
	//@ResponseBody
	public JsonResult doFindObjects() {
		return new JsonResult(sysMenuService.findObjects());
	}
	//....
	
}




