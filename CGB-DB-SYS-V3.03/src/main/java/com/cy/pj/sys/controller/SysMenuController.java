package com.cy.pj.sys.controller;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cy.pj.sys.vo.JsonResult;
import com.cy.pj.sys.po.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

/**
 * 更多控制层请求参数格式
 * https://blog.csdn.net/Brad_PiTt7/article/details/107969206
 *
 */
//@Controller
//@ResponseBody
@Api(tags = "菜单模块")
@RestController  //@Controller+@ResponseBody
@RequestMapping("/menu/")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	@ApiOperation(value = "修改菜单信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@PostMapping("doUpdateObject")
	public JsonResult doUpdateObject(@RequestBody SysMenu entity) {
		sysMenuService.updateObject(entity);
		return new JsonResult("update ok");
	}

	@ApiOperation(value = "保存菜单信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "保存成功"),
			@ApiResponse(code = 0, message = "保存失败")
	})
	@PostMapping("doSaveObject")
	public JsonResult doSaveObject(@RequestBody SysMenu entity) {
		sysMenuService.saveObject(entity);
		return new JsonResult("save ok");
	}

	@ApiOperation(value = "查询菜单信息",notes= "查询所有的菜单信息")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindZtreeMenuNodes")
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
	@PostMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysMenuService.deleteObject(id);
		return new JsonResult("delete ok");
	}

	@ApiOperation(value = "查询菜单信息",notes= "查询菜单当前节点及父节点信息")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindObjects")
	//@ResponseBody
	public JsonResult doFindObjects() {
		return new JsonResult(sysMenuService.findObjects());
	}
	//....
	
}




