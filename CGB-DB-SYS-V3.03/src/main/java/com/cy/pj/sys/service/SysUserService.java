package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.pojo.SysUserMenu;

public interface SysUserService {

	int updatePassword(String sourcePassword,String newPassword,String cfgPassword);
	
	Map<String,Object> findObjectById(Integer id);
	
	int updateObject(SysUser entity,Integer[]roleIds);
	
    int saveObject(SysUser entity,Integer[]roleIds);
    
	int validById(Integer id,Integer valid);

	PageObject<SysUserDept> findPageObjects(String username,Long pageCurrent);
}
