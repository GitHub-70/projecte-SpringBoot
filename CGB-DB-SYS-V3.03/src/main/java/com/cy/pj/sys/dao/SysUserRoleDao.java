package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 基于此dao操作用户角色关系表数据
 */
@Mapper
public interface SysUserRoleDao {
	
	
	 @Select("select role_id from sys_user_roles where user_id=#{userId}")
	 List<Integer> findRoleIdsByUserId(Integer userId);
	 
	 int insertObjects(Integer userId,Integer[]roleIds);
	 
	 @Delete("delete from sys_user_roles where user_id=#{userId}")
	 int deleteObjectsByUserId(Integer userId);
	 
	 
	 
	 
	 
}
