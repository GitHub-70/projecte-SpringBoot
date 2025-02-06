package com.cy.pj.sys.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.sys.pojo.PageObject;
import com.cy.pj.common.utils.ExcelDocmentUtil;
import com.cy.pj.common.utils.ExcelDownloadUtil;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.po.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
public class SysLogServiceImpl implements SysLogService {
	
	@Autowired
	private SysLogDao sysLogDao;
	//将来希望此业务方法参与到其它事务中执行,传播特性设置为Propagation.REQUIRED
	//将来希望此业务方法始终运行在一个独立事务中,传播特性设置为Propagation.REQUIRES_NEW
	//将写日志操作放在一个独立的事务
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Async("getSuSuAsyncTaskExecutor2") //此注解描述的方法会运行在spring框架提供的一个线程中
	/**
	 * @Async 在默认的情况下，使用的是SimpleAsyncTaskExecutor线程池，
	 * 		  该线程无法实现线程的重用，每次调用都会开启一个新的线程，若系统
	 * 		  不断地创建线程，最终会导致系统占用内存过高，引发OOM错误
	 * 		  可查看控制台日志，线程Id不断增加 ervice-thread-i
	 */
	@Override
	public void saveObject(SysLog entity) {
		String tName=Thread.currentThread().getName();
		System.out.println("SysLogService.saveObject.thread.name="+tName);
		try{Thread.sleep(2000);}catch(Exception e) {}
	    sysLogDao.insertObject(entity);
	    
	}
	/**
	 * @RequiresPermissions 为shiro框架用于描述切入点方法的一个注解对象，一旦
	 * 由这个注解对业务方法进行了描述，就表示这个业务方法必须授权才能访问。那什么
	 * 情况下才可以授权访问呢？(当用户权限标识中包含注解中定义的权限标识时。)
	 */
	@RequiresPermissions("sys:log:delete")
	@Override
	public int deleteObjects(Integer... ids) {
	    if(ids==null||ids.length==0)
	    	throw new IllegalArgumentException("请输入id的值");
	    int rows=sysLogDao.deleteObjects(ids);
	    if(rows==0)
	    	throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	
	@Override
	public PageObject<SysLog> findPageObjects(String username, Long pageCurrent) {
		//验证参数的有效性
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码值无效");//e.message="当前页码值无效"
		int  pageSize=5;//(这个值也可以从页面传递到服务端)
		//设置查询位置以及页面大小, 底层会自动将查询位置和页面大小设置到sql语句中
		Page<SysLog> page=PageHelper.startPage(pageCurrent.intValue(), pageSize);
		//查询当前页数据  这条SQL未添加 LIMIT,这个是分页插件提供的功能？
		List<SysLog> records=sysLogDao.findPageObjects(username);
		return new PageObject<>(page.getTotal(), records, pageSize, pageCurrent);
	}

	@Override
	public byte[] downloadLogReport(Long pageCurrent) throws IOException {
		int  pageSize=5;
		List<SysLog> records=sysLogDao.queryLogByPage(pageCurrent,pageSize);

		String[] rowsName = new String[]{"用户名","操作","请求方法","请求参数","IP","执行时长"};
		List<Object[]> dataRows = Lists.transform(records, sysLog -> {
			return new Object[]{
					// 属性为null时，使用Optional.ofNullable()方法给默认值
					Optional.ofNullable(sysLog.getUsername()).orElse(""),
					Optional.ofNullable(sysLog.getOperation()).orElse(""),
					Optional.ofNullable(sysLog.getMethod()).orElse(""),
					Optional.ofNullable(sysLog.getParams()).orElse(""),
					Optional.ofNullable(sysLog.getIp()).orElse(""),
					Optional.ofNullable(sysLog.getTime()).orElse(0L)
			};
		});
		// 获取一个工作簿 模板
		byte[] bytes = null;
		try (HSSFWorkbook workbook = ExcelDocmentUtil.getWorkTemplet(new HSSFWorkbook(), rowsName, dataRows, "日志报表");
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 // 单纯的在后台生成文件
			 FileOutputStream fileOutputStream = new FileOutputStream("D:\\aaa.xls")) {
			workbook.write(outputStream);
			workbook.write(fileOutputStream);
			bytes = outputStream.toByteArray();
		} catch (IOException e) {
			// 处理异常
			e.printStackTrace();
		}
		return bytes;
	}

	@Override
	public void downloadLogReport2(String userName, HttpServletResponse response) {
		try {
			// 创建导出参数
			ExportParams params = new ExportParams("操作日志", "日志列表");
			// 方式一：导出少量数据
			int pageCurrent = 1;
			int pageSize = 5;
			Page<SysLog> page1=PageHelper.startPage(pageCurrent, pageSize);
			List<SysLog> pageObjects = sysLogDao.findPageObjects(userName);
			Workbook workbook = ExcelExportUtil.exportExcel(params, SysLog.class, pageObjects);

			// 方式二：导出大量数据 queryParams 为查询参数，page 为当前页码
//			IExcelExportServer server = (queryParams, page) -> {
//				Page<SysLog> page1=PageHelper.startPage(page, 100000);
//				List<SysLog> pageObjects = sysLogDao.findPageObjects((String) queryParams);
//				if (page > page1.getPages()){
//					return null;
//				}
//				return new ArrayList<>(pageObjects);
//			};
//			Workbook workbook = ExcelExportUtil.exportBigExcel(params, SysLog.class, server, userName);

			String encodedFileName = Base64.getEncoder().encodeToString("report.xlsx".getBytes(StandardCharsets.UTF_8));
			response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// 将流设置到 response
			workbook.write(response.getOutputStream());
			// 单纯的在后台生成文件
//			FileOutputStream fileOutputStream = new FileOutputStream("D:\\aaa.xlsx");
//			workbook.write(fileOutputStream);

			// 注意：关闭工作簿前 将流设置到 response
			workbook.close();
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void downloadLogReport3(String userName, HttpServletResponse response) {
		List<SysLog> pageObjects = sysLogDao.findPageObjects(userName);
		String[] rowsName = new String[]{"用户名","操作","请求方法","请求参数","IP","执行时长"};
		List<Object[]> dataRows = Lists.transform(pageObjects, sysLog -> {
			return new Object[]{
					// 属性为null时，使用Optional.ofNullable()方法给默认值
					Optional.ofNullable(sysLog.getUsername()).orElse(""),
					Optional.ofNullable(sysLog.getOperation()).orElse(""),
					Optional.ofNullable(sysLog.getMethod()).orElse(""),
					Optional.ofNullable(sysLog.getParams()).orElse(""),
					Optional.ofNullable(sysLog.getIp()).orElse(""),
					Optional.ofNullable(sysLog.getTime()).orElse(0L)
			};
		});
		try {
//			ExcelDownloadUtil.downloadExcel(response, dataRows, rowsName, "日志列表");
			ExcelDownloadUtil.downloadExcel2(response, dataRows, rowsName, "日志列表");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}
