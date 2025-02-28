package com.cy.pj.common.utils;

import org.apache.shiro.SecurityUtils;

import com.cy.pj.sys.po.SysUser;

public class ShiroUtils {

	public static String getUsername() {
		return getUser().getUsername();
	}
	/**获取登录用户*/
	public static SysUser getUser() {
		return (SysUser)SecurityUtils.getSubject().getPrincipal();
	}
}
