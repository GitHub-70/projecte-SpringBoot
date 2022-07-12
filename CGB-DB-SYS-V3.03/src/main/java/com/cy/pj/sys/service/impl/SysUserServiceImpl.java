package com.cy.pj.sys.service.impl;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.Assert;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.common.utils.AssertUtils;
import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.service.SysUserService;
import com.github.pagehelper.util.StringUtil;

/*@Transactional 描述类表示类中所有方法都要进行事务控制,
       假如方法上也有这个注解则方法上的事务注解特性优先级比较高.
   1)readOnly属性
   1.1)含义是什么?是否为只读事务(只读事务只允许查询操作)
   1.2)默认值是什么?(false)你怎么知道的?
   2)rollbackFor属性
   1.1)含义是什么?(什么异常回滚事务)
   1.2)默认值是什么?(RuntimeException与Error,但是检查异常不回滚)
   3)noRollbackFor属性
   1.1)含义是什么?什么情况下不回滚
   1.2)默认值是什么?没有默认
   
   4)timeout 属性
   4.1)含义是什么?是否支持事务超时
   4.2)默认值是什么?-1,表示不支持事务超时,我们可以给定一个时间
   
   
 */
//@Slf4j

//@Transactional(readOnly = false,
//               rollbackFor = Exception.class,
//               timeout = 60,
//               isolation = Isolation.READ_COMMITTED)
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	
	@Override
	public int updatePassword(String sourcePassword, String newPassword, String cfgPassword) {
		//1.参数校验
//		AssertUtils.isArgValid(StringUtil.isEmpty(sourcePassword), "原密码不能为空");
//		AssertUtils.isArgValid(StringUtil.isEmpty(newPassword), "新密码不能为空");
//		AssertUtils.isArgValid(!newPassword.equals(cfgPassword), "两次新密码输入不一致");
		Assert.isTrue(!StringUtil.isEmpty(sourcePassword), MessageFormat.format("原密码 {0} can not null", sourcePassword));
//		AssertUtils.isArgValid(StringUtil.isEmpty(sourcePassword), MessageFormat.format("原密码 {0} can not null", sourcePassword));
		AssertUtils.isArgValid(StringUtil.isEmpty(newPassword), MessageFormat.format("新密码 {0} can not null", newPassword));
		AssertUtils.isArgValid(!newPassword.equals(cfgPassword), MessageFormat.format("两次密码{0},{1}输入不一致", newPassword, cfgPassword));

		//验证原密码是否正确
		SysUser user=ShiroUtils.getUser();
		String hashedPwd=user.getPassword();
		SimpleHash sh=new SimpleHash("MD5",sourcePassword, user.getSalt(), 1);
		AssertUtils.isArgValid(!hashedPwd.equals(sh.toHex()), "原密码输入不正确");
		//2.修改密码
		//2.1加密新密码
		String newSalt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5",newPassword,newSalt, 1);
		String newHashedPwd=sh.toHex();
		//2.2更新新密码
		int rows=sysUserDao.updatePassword(newHashedPwd, newSalt,user.getId());
		return rows;
	}
	
	// https://blog.csdn.net/weixin_44299027/article/details/95231808
	@Transactional(rollbackFor = IllegalArgumentException.class)//由此注解描述的方法为一个事务切入点方法,后续运行时会对它描述的描述的方法进行事务控制
	@RequiredLog(value="自定义注解方法的描述--禁用启用")//此注解描述的方法为一个日志切入点方法
	@Override
	public int validById(Integer id, Integer valid){
		//1.参数校验
		AssertUtils.isArgValid(id==null||id<1, "参数值无效");
		AssertUtils.isArgValid(valid != 0 && valid != 1, "状态值无效");
		//2.修改用户状态
		SysUser user=ShiroUtils.getUser();
		int rows=sysUserDao.validById(id, valid, user.getUsername());//这里的admin先假设为登录用户
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	
	/**
	 * 由于只读事务不存在数据的修改，因此数据库将会为只读事务提供一些优化手段，
	 * 例如Oracle对于只读事务，不启动回滚段，不记录回滚log
	 * 但只读事务不允许，增删改操作；在其他增删改方法，就不能调用其查询方法--TODO 待验证
	 * 当多个查询方法在一起时，建议用@Transactional(readOnly = true)
	 * 保证数据的一致性
	 */
	@Transactional(readOnly = true) //所有的查询方法建议readOnly=true (性能会比较高)
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//long t1=System.currentTimeMillis();//直接在类方法中添加代码会违背开闭原则
		//1.参数校验
		AssertUtils.isArgValid(id==null||id<1,"id值无效");
		//2.查询用户以及用户对应的部门信息
		SysUserDept user=sysUserDao.findObjectById(id);
		AssertUtils.isServiceValid(user==null, "记录可能已经不存在");
		//3.查询用户对应的角色信息
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//4.封装查询结果
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		//long t2=System.currentTimeMillis();
		//log.info("time {}",t2-t1);
		return map;
	}
	
	// 注意类上加了@Transactional 注解
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	@RequiredLog(value="自定义注解方法的描述--保存用户")
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) throws Exception {
		//1.参数校验
		AssertUtils.isArgValid(entity==null, "保存对象不能为空");
		AssertUtils.isArgValid(entity.getUsername()==null||"".equals(entity.getUsername()), "用户名不能为空");
		AssertUtils.isArgValid(entity.getPassword()==null||"".equals(entity.getPassword()), "密码不能为空");
		AssertUtils.isArgValid(roleIds==null||roleIds.length==0, "必须为用户分配角色");
		//2.保存用户自身信息
		//2.1 构建salt(盐)值
		String salt=UUID.randomUUID().toString();//产生一个固定长度随机字符串
		//2.2 基于相关API对密码进行加密
		//借助spring框架中自带API对密码进行加密
		//String hashedPassword=
		//DigestUtils.md5DigestAsHex((salt+entity.getPassword()).getBytes());
		//借助shiro框架中的API对密码进行加密
		SimpleHash sh=new SimpleHash("MD5",//算法名称
				          entity.getPassword(), //未加密的密码
				          salt,//加密盐 
				          1);//这里1表示加密次数
		String hashedPassword=sh.toHex();//将加密结果转换为16进制的字符串
		entity.setSalt(salt);//为什么盐值也要保存到数据库?(登录时还要使用此salt对登录密码进行加密)
		entity.setPassword(hashedPassword);
		//2.3 将用户信息写入到数据库
		int rows=sysUserDao.insertObject(entity);
		//3.保存用户与角色关系数据
		// 抛出RuntimeException或其子类 异常，回滚事务
//		System.out.println(1/0);// 模拟出错
//		if(true) {
//			// 抛出Exception或其子类 异常，不会回滚事务,除非将该事务rollbackFor定义为Exception
//			throw new Exception("自定义Exception 异常测试事务");
//		}
		
//		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		
		// test1 外部有事务，内部事务新起事务，不处理异常，外部事务处理异常，不影响外部事务
		try {
			SysUserServiceImpl currentProxy = (SysUserServiceImpl)AopContext.currentProxy();
			currentProxy.transationalTest(entity, roleIds);
		} catch (Exception e) {
			System.out.println("内部事务出现异常，外部处理，不影响外部事务");
		}
		
		// test2 同一个类事务方法调用。外部有事务，内部事务新起事务，不处理异常，外部事务处理异常，该内部事务不生效
//		try {
//			transationalTest(entity, roleIds);
//		} catch (Exception e) {
//			System.out.println("内部事务出现异常，外部处理，该事务不生效");
//		}
		
		// test3 外部有事务，内部事务处理异常，内部事务不生效
//		SysUserServiceImpl currentProxy = (SysUserServiceImpl)AopContext.currentProxy();
//		currentProxy.transationalTestHandlerExeption(entity, roleIds);
		
		// test4 外部无事务，内部事务不处理异常，内部事务生效，不影响之前操作
		// 同类中的方法调用，外部无事务，使用AopContext生成要生成的动态代理类，使内部事务生效，注意启动类要加特定注解
//		SysUserServiceImpl currentProxy = (SysUserServiceImpl)AopContext.currentProxy();
//		currentProxy.transationalTest(entity, roleIds);
		return rows;
	}
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)// 两个保存为同一事物
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		AssertUtils.isArgValid(entity==null, "保存对象不能为空");
		AssertUtils.isArgValid(entity.getUsername()==null||"".equals(entity.getUsername()), "用户名不能为空");
		AssertUtils.isArgValid(roleIds==null||roleIds.length==0, "必须为用户分配角色");
		//2.保存用户自身信息
//		int rows=sysUserDao.updateObject(entity);
		
		// test1 同一类的事务方法调用，内部事务不生效
//		int rows=transationalUpdateTest(entity);
		
		// test2 外部事务出现异常，外部事务回滚，不影响内部事务
		SysUserServiceImpl currentProxy = (SysUserServiceImpl)AopContext.currentProxy();
		int rows=currentProxy.transationalUpdateTest(entity);
		
		AssertUtils.isServiceValid(rows==0, "记录可能已经不存在了");
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		System.out.println(1/0);// 模拟出错
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		return rows;
	}

	@Transactional(readOnly = true)
	@RequiredLog(value="自定义注解方法的描述--分页查询")
	@Override
	public PageObject<SysUserDept> findPageObjects(String username, Long pageCurrent) {
		String tName=Thread.currentThread().getName();
		System.out.println("SysUserService.findPageObjects.thread.name="+tName);
		//1.参数有效性校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值无效");
		//2.查询总记录数并校验
		long rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=2;
		long startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDept> records=
				sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(rowCount, records, pageSize, pageCurrent);
	}

	/**
	 * 为了测试，内部事务出现异常，不影响外部事务
	 * @param entity
	 * @param roleIds
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void transationalTest(SysUser entity, Integer[] roleIds) {
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		System.out.println(1/0);// 模拟出错
	}
	
	/**
	 * 为了测试，内部新起事务，内部处理事务异常，内部事务不生效
	 * @param entity
	 * @param roleIds
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void transationalTestHandlerExeption(SysUser entity, Integer[] roleIds) {
		try {
			sysUserRoleDao.insertObjects(entity.getId(), roleIds);
			System.out.println(1/0);// 模拟出错
		} catch (RuntimeException e) {
			System.out.println("事务测试，内部事务为新事务，出现异常，不向外抛异常");
		}
	}
	
	/**
	 * 为了测试，内部新起事务，外部事务出现异常
	 * @param entity
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public int transationalUpdateTest(SysUser entity) {
		int rows=sysUserDao.updateObject(entity);
		entity.setModifiedUser("myself1");
		int rows2=sysUserDao.updateObject(entity);
		return rows2;
	}
}
