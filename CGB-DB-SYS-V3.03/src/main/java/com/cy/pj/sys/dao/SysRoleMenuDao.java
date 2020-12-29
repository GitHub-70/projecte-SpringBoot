package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *	 基于此对象操作角色和菜单关系表(sys_role_menus)数据
 */
@Mapper
public interface SysRoleMenuDao {
	 List<Integer> findMenuIdsByRoleIds(List<Integer> roleIds);
	 /**
	  * 	基于角色id获取菜单id
	  * @param id
	  * @return
	  */
	 @Select("select menu_id from sys_role_menus where role_id=#{id}")
	 List<Integer> findMenuIdsByRoleId(Integer id);

	 int insertObjects(Integer roleId,Integer[] menuIds);
	 
	 /**
	  * 	基于角色id删除角色和菜单关系数据
	  * @param menuId
	  * @return
	  */
	 @Delete("delete from sys_role_menus where role_id=#{roleId}")
	 int deleteObjectsByRoleId(Integer roleId);
     /**
      * 	基于菜单id删除角色和菜单关系数据
      * @param menuId
      * @return
      */
	 @Delete("delete from sys_role_menus where menu_id=#{menuId}")
	 int deleteObjectsByMenuId(Integer menuId);
}
