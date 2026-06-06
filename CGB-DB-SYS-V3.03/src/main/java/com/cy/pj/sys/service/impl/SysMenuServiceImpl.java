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
import com.cy.pj.sys.bo.SysUserMenu;
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
	 *	@Cacheable 注解的主要属性
	 * 		value/name：指定缓存的名称，必填项。
	 * 		key：缓存数据的key，默认使用方法参数组合。
	 * 		condition：满足条件才缓存。
	 * 		unless：满足条件不缓存。
	 * 		keyGenerator：自定义key生成器。
	 * 		cacheManager：指定使用的缓存管理器。
	 *
	 * 	Spring默认的缓存 key生成规则：
	 * 		1.如果方法没有参数，那么key为 SimpleKey.EMPTY
	 * 		2.如果方法有一个参数，那么key为该参数值
	 * 		3.如果方法有多个参数，那么key为包含所有参数的SimpleKey对象
	 *
	 * 使用注意事项
	 *  	1.必须开启缓存：需要在配置类或启动类上添加 @EnableCaching 注解。
	 * 		2.缓存一致性：当数据发生变化时（增删改），需要使用 @CacheEvict 或 @CachePut 清除或更新缓存，
	 * 		确保缓存数据与实际数据的一致性。项目中在修改、新增、删除菜单时都使用了 @CacheEvict 注解清除菜单缓存。
	 * 		3.key 的设计：默认情况下，Spring 使用方法参数作为缓存的 key，但对于复杂对象，需要自定义 key 策略。
	 * 		4.缓存管理器：Spring Boot 默认使用 ConcurrentMap 作为缓存存储，生产环境建议使用 Redis、Ehcache 等专业缓存组件。
	 * 		5.异常处理：当方法执行抛出异常时，不会缓存结果。
	 * 		6.同步问题：在高并发场景下，可以考虑使用 sync = true 属性来避免缓存击穿：
	 *
	 * 	相关注解
	 * 		@CacheEvict：用于清除缓存，通常用于更新或删除操作后。
	 * 		@CachePut：无论缓存中是否存在，都会执行方法并更新缓存。
	 * 		@Caching：可以组合多个 @Cacheable、@CacheEvict 或 @CachePut 注解。
	 * 		@CacheConfig：类级别的注解，用于统一设置缓存配置。
	 */
    @Cacheable(value = "menuCache") //此注解描述的方法为一个缓存切入点方法
	@Override
	public List<SysMenu> findObjects() {
		return sysMenuDao.findObjects();
	}
}
