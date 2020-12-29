package com.cy.pj.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.Node;
import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<SysUserMenu> findUserMenus(Integer userId) {
		//1.基于用户id找角色id
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(userId);
		//2.基于角色id找菜单id
		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
		//3.查找用户对应的菜单信息
		return sysMenuDao.findUserMenus(menuIds);
	}
	
//	@Override
//	public List<SysUserMenu> findUserMenus(Integer userId) {
//		//获取所有菜单
//        List<SysMenu> menus=sysMenuDao.findObjects();
//        //从所有菜单中提取用户菜单
//        List<SysUserMenu> userMenus=new ArrayList<SysUserMenu>();
//        //迭代所有菜单，然后将用户拥有访问权限的菜单存储到userMenus中
//        //.....???????
//        return userMenus;
//	}
	
	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int updateObject(SysMenu entity) {
	    //1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))//org.springframework.util.StringUtils
			throw new IllegalArgumentException("菜单名不允许为空");
		//....
		//2.保存菜单信息
		int rows=sysMenuDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在了");
		return rows;
	}
	
	
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))//org.springframework.util.StringUtils
			throw new IllegalArgumentException("菜单名不允许为空");
		//....
		//2.保存菜单信息
		int rows=sysMenuDao.insertObject(entity);
		return rows;
	}
	
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	
	
	@Override
	public int deleteObject(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("id值无效");
		//2.查询子菜单个数
		int childCount=sysMenuDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("请先删除子菜单");
		//3.删除菜单信息
		//3.1删除关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//3.2删除自身信息
		int rows=sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在了");
		return rows;
	}
    @Cacheable(value = "menuCache") //此注解描述的方法为一个缓存切入点方法
	@Override
	public List<SysMenu> findObjects() {
		return sysMenuDao.findObjects();
	}
}
