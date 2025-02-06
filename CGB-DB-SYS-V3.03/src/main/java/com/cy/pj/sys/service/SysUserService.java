package com.cy.pj.sys.service;

import java.util.Map;

import com.cy.pj.sys.pojo.PageObject;
import com.cy.pj.sys.po.SysUser;
import com.cy.pj.sys.po.SysUserDept;

public interface SysUserService {

	int updatePassword(String sourcePassword,String newPassword,String cfgPassword);
	
	Map<String,Object> findObjectById(Integer id);
	
	int updateObject(SysUser entity,Integer[]roleIds);
	
    int saveObject(SysUser entity,Integer[]roleIds) throws Exception;
    
	int validById(Integer id,Integer valid);

	PageObject<SysUserDept> findPageObjects(String username,Long pageCurrent);
}
