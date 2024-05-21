package com.cy.pj.sys.controller;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cy.pj.sys.service.SysLogService;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.common.pojo.PageObject;

@Api(tags = "日志模块")
@Controller
@RequestMapping("/log/")
public class SysLogController {
	
	@Autowired
	private SysLogService sysLogService;

	@ApiOperation(value = "删除日志信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ids", value = "日志ids", required = true, dataType = "Integer")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "删除成功"),
			@ApiResponse(code = 0, message = "删除失败")
	})
	@RequestMapping("doDeleteObjects")
	@ResponseBody
	public JsonResult doDeleteObjects(Integer... ids){
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete ok");
	}

	@ApiOperation(value = "分页查询日志信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
			@ApiImplicitParam(name = "pageCurrent", value = "当前页", required = true, dataType = "Long")
	})
	@ApiResponses({
			@ApiResponse(code = 1, message = "查询成功"),
			@ApiResponse(code = 0, message = "查询失败")
	})
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(String username,Long pageCurrent) {
//		try {
			PageObject<SysLog> pageObject=
					sysLogService.findPageObjects(username, pageCurrent);
			return new JsonResult(pageObject);
//		}catch(Throwable e) {
//			return new JsonResult(e);
//		}
	}
	//思考:假如控制层有多个方法,每个方法都要进行try操作,其实也是一个重复的劳动.

}





