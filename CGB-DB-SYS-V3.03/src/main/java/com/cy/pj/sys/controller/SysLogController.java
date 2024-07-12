package com.cy.pj.sys.controller;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.cy.pj.sys.service.SysLogService;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.common.pojo.PageObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
	@PostMapping("doDeleteObjects")
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
	@GetMapping("doFindPageObjects")
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


	@ApiOperation(value = "导出日志信息",notes= "方法的备注说明")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageCurrent", value = "当前页", required = true, dataType = "Long")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "导出成功"),
			@ApiResponse(code = 500, message = "服务器内部错误，导出失败")
	})
	@GetMapping("/download/report")
	public ResponseEntity<byte[]> downloadLogReport(HttpServletResponse response, @RequestParam Long pageCurrent) throws IOException {
		// 设置响应头
		try {
			byte[] bytes = sysLogService.downloadLogReport(pageCurrent);

			if (bytes == null || bytes.length == 0) {
				return ResponseEntity.noContent().build();
			}

			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//			String encodedFileName = URLEncoder.encode("report.xlsx", "utf-8").replace("+", "%20");
//			headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
//			String encodedFileName = URLEncoder.encode("report.xls", "utf-8").replace("+", "%20");
//			headers.setContentDispositionFormData("attachment", encodedFileName);

			return ResponseEntity.ok().headers(headers).body(bytes);
		} catch (IOException e) {
			// Log the exception and return an appropriate error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}





