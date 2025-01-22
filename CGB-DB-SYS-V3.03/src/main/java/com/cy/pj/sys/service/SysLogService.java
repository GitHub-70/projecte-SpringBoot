package com.cy.pj.sys.service;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.po.SysLog;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
   * 现在的项目都会分层设计，每次对象都会对应的接口，层与层对象进行耦合时
   * 建议耦合与接口。
 */
public interface SysLogService {
	
	
	void saveObject(SysLog entity);
	
	int deleteObjects(Integer...ids);
	
    /**
     * 	分页查询日志信息
     * @param username 用户名
     * @param pageCurrent 当前页页码
     * @return 封装了查询和计算结果的一个分页对象
     */
	PageObject<SysLog> findPageObjects(String username,Long pageCurrent);

	/**
	 * 下载日志报表
	 * @param pageCurrent
	 * @return byte
	 * @throws IOException
	 */
	byte[] downloadLogReport(Long pageCurrent) throws IOException;

	/**
	 * 下载日志报表
	 * @param userName
	 * @return Workbook
	 */
	void downloadLogReport2(String userName, HttpServletResponse response);
	/**
	 * 下载日志报表
	 * @param userName
	 * @return Workbook
	 */
	void downloadLogReport3(String userName, HttpServletResponse response);
}
