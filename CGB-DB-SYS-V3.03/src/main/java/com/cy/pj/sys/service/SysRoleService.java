package com.cy.pj.sys.service;
import java.util.List;

import com.cy.pj.sys.dto.CheckBox;
import com.cy.pj.sys.pojo.PageObject;
import com.cy.pj.sys.po.SysRole;
import com.cy.pj.sys.po.SysRoleMenu;

public interface SysRoleService {
	List<CheckBox> findRoles();
	
	 /**
	  * 	基于角色id获取角色以及角色对应的菜单id
	  * @param id
	  * @return
	  */
	 SysRoleMenu findObjectById(Integer id);
	
	 int updateObject(SysRole entity,Integer[] menuIds);
	 
	 int saveObject(SysRole entity,Integer[] menuIds);
	 /**
	  * 	查询当前页角色信息
	  * @param name
	  * @param pageCurrent
	  * @return
	  */
	 PageObject<SysRole> findPageObjects(String name,Long pageCurrent);
}
