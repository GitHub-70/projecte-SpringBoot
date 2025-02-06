package com.cy.pj.sys.dao;
import java.util.*;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.sys.po.*;

@Mapper
public interface SysRoleDao {//sys_roles
	/**
	 * 	查询所有角色id和name
	 * @return
	 */
	@Select("select id,name from sys_roles")
	List<SysRole> findRoles();
	
	int updateObject(SysRole entity);
	/**
	  *  基于角色id查找角色自身信息
	 * @param id
	 * @return
	 */
	//@Select("select id,name,note from sys_roles where id=#{id}")
	SysRoleMenu findObjectById(Integer id);
	/**
	 * 	保存角色自身信息
	 * @param entity
	 * @return
	 */
	@Insert("insert into sys_roles (name,note,createdUser,modifiedUser,createdTime,modifiedTime) values (#{name},#{note},#{createdUser},#{modifiedUser},now(),now())")
	@Options(useGeneratedKeys = true,keyProperty = "id")
	int insertObject(SysRole entity);
	
	/**
	 * 	基于角色名统计查询角色相关信息
	 * @param name 角色名
	 * @return 统计的个数 
	 */
	long getRowCount(String name);
	/**
	 * 	按条件从指定位置查询当前页角色信息
	 * @param name 角色名
	 * @param startIndex 起始位置
	 * @param pageSize 页面大小
	 * @return 当前页的角色信息
	 */
	List<SysRole> findPageObjects(String name,Long startIndex,Integer pageSize);
}
