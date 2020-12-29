package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.CheckBox;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;
import com.cy.pj.sys.service.SysRoleService;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Override
	public List<CheckBox> findRoles() {
		// TODO Auto-generated method stub
		return sysRoleDao.findRoles();
	}
	
	@Override
	public SysRoleMenu findObjectById(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("id值无效");
		//2.基于角色id查询角色自身信息
		SysRoleMenu rm=sysRoleDao.findObjectById(id);//sys_roles
		//3.基于角色id查询角色对应的菜单id并进行封装
		//List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);//sys_role_menus
		//rm.setMenuIds(menuIds);
		//4.返回查询结果
		return rm;
	}
	
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))//StringUtil为spring中的一个工具类
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new IllegalArgumentException("比为角色分配权限");
		//2.更新角色自身信息
		int rows=sysRoleDao.updateObject(entity);
		//3.更新角色菜单关系数据
		//3.1删除原有关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
		//3.2添加新的关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))//StringUtil为spring中的一个工具类
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new IllegalArgumentException("比为角色分配权限");
		//2.保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//3.保存角色菜单关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}

	@Override
	public PageObject<SysRole> findPageObjects(String name, Long pageCurrent) {
		//1.参数有效性校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值无效");
		//2.查询总记录数并校验
		long rowCount=sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=2;
		long startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records=
		sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(rowCount, records, pageSize, pageCurrent);
	}

}
