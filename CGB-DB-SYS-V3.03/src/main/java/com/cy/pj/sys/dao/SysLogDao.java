package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.pojo.SysLog;

@Mapper
public interface SysLogDao {

    int insertObject(SysLog entity);

    /**
     * 基于id删除多条日志信息
     *
     * @param ids
     * @return
     */
    int deleteObjects(Integer... ids);

    /**
     * 基于条件查询当前页数据
     *
     * @param username   查询条件
     * @param startIndex 起始位置
     * @param pageSize   页面大小(每页最多显示多少条记录)
     * @return 查询到的记录
     */
    List<SysLog> findPageObjects(String username);

    /**
     * 日志分页查询
     * @param pageCurrent
     * @param pageSize
     * @return List
     */
    List<SysLog> queryLogByPage(Long pageCurrent, int pageSize);
}
