package com.cy.pj.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;

//@Service
public class ShiroUserRealm extends AuthorizingRealm {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysMenuDao sysMenuDao;
	
	
	
	/**重写获取加密匹配器对象的方法*/
	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
		cMatcher.setHashAlgorithmName("MD5");
		cMatcher.setHashIterations(1);
		return cMatcher;
	}
//	@Override
//	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
//		cMatcher.setHashAlgorithmName("MD5");
//		cMatcher.setHashIterations(1);
//		super.setCredentialsMatcher(cMatcher);
//	}
	
	/**负责-认证信息的-获取和封装*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.out.println("==doGetAuthenticationInfo信息认证==");
		//1.获取登录用户名
		UsernamePasswordToken uToken=(UsernamePasswordToken)token;
		String username=uToken.getUsername();
		//2.基于用户名查询数据并校验
		SysUser user=sysUserDao.findUserByUserName(username);
		if(user==null)throw new UnknownAccountException();
		//3.校验用户是否被禁用
		if(user.getValid()==0)throw new LockedAccountException();
		//4.封装用户信息并返回
		ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info=
		   new SimpleAuthenticationInfo(
				user,//principal 表示身份 可通过SecurityUtils.getSubject().getPrincipal()获取
				user.getPassword(),//hashedCredentials 已加密的密码
				credentialsSalt, //credentialsSalt 加密盐
				getName());//realmName
		return info;//这个对象会返回给securityManager对象,然后进行认证分析
	}
	
	/**负责-授权信息的-获取和封装*/
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("==doGetAuthorizationInfo授权信息==");
		//1.获取登录用户的用户ID
		SysUser user=(SysUser)principals.getPrimaryPrincipal();
		//2.基于登录用户id查询用户角色id并校验
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于角色id查询角色对应的菜单id并校验
		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id查询菜单中的授权标识并校验
		List<String> permissions=sysMenuDao.findPermissions(menuIds);
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.封装查询结果并返回(交给SecurityManager对象)
		Set<String> stringPermissions=new HashSet<>();
		for(String per:permissions) {
			if(!StringUtils.isEmpty(per)) {//StringUtils为spring框架工具类
				stringPermissions.add(per);
			}
		}
		System.out.println("stringPermissions="+stringPermissions);
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		info.setStringPermissions(stringPermissions);
		return info;
	}


}







