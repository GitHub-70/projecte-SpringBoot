package com.cy.pj.sys.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.po.SysDept;
import com.cy.pj.sys.service.SysDeptService;

@Api(tags = "部门模块")
@RestController
@RequestMapping("/dept/")
public class SysDeptController {
	@Autowired
	private SysDeptService sysDeptService;

	@ApiOperation(value = "查询部门信息",notes= "查询当前部门及上级部门信息")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindObjects")
	public JsonResult doFindObjects() {
		return new JsonResult(sysDeptService.findObjects());
	}

	@ApiOperation(value = "查询部门信息",notes= "查询所有的部门信息")
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@GetMapping("doFindZTreeNodes")
	public JsonResult doFindZTreeNodes() {
		return new JsonResult(sysDeptService.findZTreeNodes());
	}

	@ApiOperation(value = "修改部门信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "修改成功"),
			@ApiResponse(code = 0, message = "修改失败")
	})
	@PostMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysDept entity){
		sysDeptService.updateObject(entity);
	    return new JsonResult("update ok");
	}

	@ApiOperation(value = "保存部门信息",notes= "方法的备注说明")
	@ApiResponses({
			@ApiResponse(code = 1, message = "保存成功"),
			@ApiResponse(code = 0, message = "保存失败")
	})
	@PostMapping("doSaveObject")
	public JsonResult doSaveObject(SysDept entity){
		sysDeptService.saveObject(entity);
		return new JsonResult("save ok");
	}

	@ApiOperation(value = "删除部门信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "部门id", required = true, dataType = "Integer")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "删除成功"),
			@ApiResponse(code = 0, message = "删除失败")
	})
	@PostMapping("doDeleteObject")
	@ResponseBody
	public JsonResult doDeleteObject(Integer id){
		sysDeptService.deleteObject(id);
		return new JsonResult("delete ok");
	}
}
