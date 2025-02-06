package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.sys.dto.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.po.SysMenu;
import com.cy.pj.sys.po.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

//	@Autowired
	private SysMenuDao sysMenuDao;
	
//	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
//	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	/**
	 * 通过构造方法注入服务类
	 * @param sysMenuDao
	 * @param sysRoleMenuDao
	 * @param sysUserRoleDao
	 */
	@Autowired
	public SysMenuServiceImpl (SysMenuDao sysMenuDao,
								SysRoleMenuDao sysRoleMenuDao,
								SysUserRoleDao sysUserRoleDao) {
		this.sysMenuDao = sysMenuDao;
		this.sysRoleMenuDao = sysRoleMenuDao;
		this.sysUserRoleDao = sysUserRoleDao;
		
	}
	
	/**
	 * 此处可优化成一个联合查询，只需连接数据库一次
	 */
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
	/**
	 * @CacheEvict
	 * 	--allEntries = true 清除名为menuCache的所有缓存
	 * 	作用：用于标记一个方法，表示在方法执行后需要清除缓存中的某些数据。可以用于删除或更新操作，
	 * 	以确保缓存中的数据与实际数据保持一致。
	 *  使用场景：适用于写操作（如插入、更新、删除），确保缓存中的数据不会过时。
	 */
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
	/**
	 * @CacheEvict
	 * 	--allEntries = true 清除名为menuCache的所有缓存
	 * 	作用：用于标记一个方法，表示在方法执行后需要清除缓存中的某些数据。可以用于删除或更新操作，
	 * 	以确保缓存中的数据与实际数据保持一致。
	 *  使用场景：适用于写操作（如插入、更新、删除），确保缓存中的数据不会过时。
	 */
	@CacheEvict(value = "menuCache",allEntries = true)
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
		List<SysMenu> menuNodes = sysMenuDao.findZtreeMenuNodes();
		// 1.将查询结果转换为List<Node>
		List<Node> nodeList = menuNodes.stream().map(sysMenu -> {
			Node node = new Node();
			node.setId(sysMenu.getId());
			node.setParentId(sysMenu.getParentId());
			node.setName(sysMenu.getName());
			return node;
		}).collect(Collectors.toList());
		return nodeList;
	}

	/**
	 * @CacheEvict
	 * 	--allEntries = true 清除名为menuCache的所有缓存
	 * 	作用：用于标记一个方法，表示在方法执行后需要清除缓存中的某些数据。可以用于删除或更新操作，
	 * 	以确保缓存中的数据与实际数据保持一致。
	 *  使用场景：适用于写操作（如插入、更新、删除），确保缓存中的数据不会过时。
	 */
	@CacheEvict(value = "menuCache", allEntries = true/*, key = "#id"*/)
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
	/**
	 * @Cacheable
	 * 		--该方法会去找key为menuCache 的缓存
	 * 		--查询的结果会放入 menuCache 缓存中
	 *
	 * 	作用：用于标记一个方法，表示该方法的返回结果可以被缓存。当方法被调用时，
	 * 	Spring 会首先检查缓存中是否存在该方法的结果，如果存在则直接返回缓存的结果，
	 * 	否则执行方法并将结果存入缓存。
	 *
	 *  使用场景：适用于读取操作，特别是那些计算成本高或者数据不经常变化的方法。
	 */
    @Cacheable(value = "menuCache") //此注解描述的方法为一个缓存切入点方法
	@Override
	public List<SysMenu> findObjects() {
		return sysMenuDao.findObjects();
	}
}
