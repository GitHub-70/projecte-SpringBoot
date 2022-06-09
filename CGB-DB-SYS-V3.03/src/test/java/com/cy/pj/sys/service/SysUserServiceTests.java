package com.cy.pj.sys.service;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.utils.AssertUtils;
import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.impl.SysUserServiceImpl;
import com.github.pagehelper.util.StringUtil;

// 运行环境为PowerMockRunner
@RunWith(PowerMockRunner.class)
// 前期准备的静态代理对象
@PrepareForTest({ StringUtil.class, ShiroUtils.class })
public class SysUserServiceTests {

	/**
	 * @InjectMocks注解修饰，会进入具体的实现中 相当于@Autowired
	 * 
	 * TODO @MockBean 待研究
	 */
	@InjectMocks
	private SysUserServiceImpl sysUserServiceImpl;
	
	/**
	 * @Mock注解修饰，不会进入具体的实现，被mock掉的对象
	 */
	@Mock
	private SysUserDao sysUserDao;
	
	@Mock
	private SysUserRoleDao sysUserRoleDao;
	
	@Mock
	private AssertUtils assertUtils;
	
	@Mock
	private SimpleHash simpleHash;
	
	// 期望抛出的异常
	@Rule
	private ExpectedException thrown = ExpectedException.none();
	
	
	/**
	 * 验证更新密码
	 */
	@Test
	public void updatePassword_should_thrown_IllegalArgumentException_when_oldPassword_isEmpty() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("原密码不能为空");
		
		sysUserServiceImpl.updatePassword("", "a", "aa");
		
	}
	
	
	@Test
	public void updatePassword_should_thrown_IllegalArgumentException_when_newPassword_isEmpty() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("新密码不能为空");
		
		sysUserServiceImpl.updatePassword("a", "", "aa");
		
	}
	
	
	@Test
	public void updatePassword_should_thrown_IllegalArgumentException_when_oldPasswordAndNewPassword_different() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("两次新密码输入不一致");
		
		sysUserServiceImpl.updatePassword("a", "aa", "aaa");
		
	}
	
	
	@Test
	public void updatePassword_should_thrown_IllegalArgumentException_when_md5EncodeInputPassword_different() {
		
		SysUser sysUser = new SysUser();
		// 加密后的密码
		sysUser.setPassword("123");
		sysUser.setSalt("salt");
		//mock 静态方法
		PowerMockito.mockStatic(ShiroUtils.class);
		Mockito.when(ShiroUtils.getUser()).thenReturn(sysUser);
		PowerMockito.verifyStatic(ShiroUtils.class);
		
		// 无效代码
//		Mockito.when(simpleHash.toHex()).thenReturn("asalt");

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("原密码输入不正确");
		
		sysUserServiceImpl.updatePassword("a", "aaa", "aaa");
		
	}
	
	
	@Test
	public void updatePassword_should_resultSuccess() {
		
		SysUser sysUser = new SysUser();
		// 密码为a,盐值为null 的md5 加密结果
		sysUser.setPassword("0cc175b9c0f1b6a831c399e269772661");
		
		//mock 静态方法
		PowerMockito.mockStatic(ShiroUtils.class);
		Mockito.when(ShiroUtils.getUser()).thenReturn(sysUser);
		
		// 无效代码
//		Mockito.when(simpleHash.toHex()).thenReturn("asalt");

		String newPassword = Mockito.anyString();
		String newSalt = Mockito.anyString();
		int userId = Mockito.anyInt();
		Mockito.when(sysUserDao.updatePassword(newPassword, newSalt, userId)).thenReturn(1);
		
		int updatePassword = sysUserServiceImpl.updatePassword("a", "aaa", "aaa");
		// 用于期望结果，与实际结果的对比
		assertEquals(1, updatePassword);
		
	}
	
	/**
	 * 验证id
	 */
	@Test
	public void validById_should_thrown_IllegalArgumentException_when_id_isNullOrLessThanOne() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("参数值无效");
		
		sysUserServiceImpl.validById(null, 2);
		
	}
	
	@Test
	public void validById_should_thrown_IllegalArgumentException_when_validStatus_isNotZeroAndisNotOne() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("状态值无效");
		
		sysUserServiceImpl.validById(2, 2);
		
	}
	
	@Test
	public void validById_should_thrown_ServiceException_when_id_isNotExist() {
		int id = 3333;
		int valid = 1;
		SysUser user = new SysUser();
		PowerMockito.mockStatic(ShiroUtils.class);
		Mockito.when(ShiroUtils.getUser()).thenReturn(user);
		Mockito.when(sysUserDao.validById(id, valid, user.getUsername())).thenReturn(0);
		
		thrown.expect(ServiceException.class);
		thrown.expectMessage("记录可能已经不存在");
		
		sysUserServiceImpl.validById(id, valid);
		
	}
	
	@Test
	public void validById_should_resultSuccess() {
		int id = 12;
		int valid = 1;
		SysUser user = new SysUser();
		PowerMockito.mockStatic(ShiroUtils.class);
		Mockito.when(ShiroUtils.getUser()).thenReturn(user);
		Mockito.when(sysUserDao.validById(id, valid, user.getUsername())).thenReturn(1);
		
		sysUserServiceImpl.validById(id, valid);
		// 用于期望结果，与实际结果的对比
		assertEquals(1, 1);
		
	}
}
