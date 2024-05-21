package com.cy.pj.common.config;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.sys.service.realm.ShiroUserRealm;
/**
 * @Configuration 注解描述的类为spring容器中的配置类,
 * 	此类的实例也会交给spring管理
 *     基于:http://shiro.apache.org/spring-boot.html中,shiro配置说明进行配置.
 *
 * 启动报错:
 *    https://blog.csdn.net/qq_37186947/article/details/103847945
 */
@Configuration
public class SpringShiroConfig {
	@Bean
	public Realm realm() {
	  return new ShiroUserRealm();
	}

	/**
	 * Authorizer 是一个接口，提供了一个简单的 isPermitted() 方法，
	 * 该方法将 Permission 对象作为参数。Permission 对象表示请求的操作或资源。
	 * Authorizer 实现然后评估主题的权限，并返回 true 如果操作被允许，或者 false 否则。
	 *
	 * 此方式，当密码错误抛出的异常 非预期异常
	 */
//	@Bean
//	public Authorizer authorizer(){
//		return new ModularRealmAuthorizer();
//	}

	/**
	 * SecurityManager (安全管理器)
	 * 配置Shiro的SecurityManager
	 * @return
	 *
	 * SecurityManager 是 Shiro 安全框架的中心组件。它负责管理整个安全生命周期，包括：
	 * 身份验证：验证主题的身份
	 * 授权：确定主题是否具有必要的权限
	 * 会话管理：管理主题的会话和关联数据
	 * 领域管理：与数据源（例如数据库、LDAP）交互以检索用户数据和权限
	 *
	 * SecurityManager 作为一个门面，提供了一个单一的入口点来与安全相关的操作。
	 * 它协调了各种安全组件之间的交互，包括 Authorizer、Authenticator 和 Realm。
	 */
	@Bean
	public SessionsSecurityManager securityManager() {
		DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
		defaultSecurityManager.setRealm(realm());
		return  defaultSecurityManager;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
	    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
	    // 放行资源 用anon
	    chainDefinition.addPathDefinition("/bower_components/**", "anon");
	    chainDefinition.addPathDefinition("/build/**", "anon");
	    chainDefinition.addPathDefinition("/dist/**", "anon");
	    chainDefinition.addPathDefinition("/plugins/**", "anon");
	    chainDefinition.addPathDefinition("/base64-js-master/**", "anon");
	    chainDefinition.addPathDefinition("/jquerybase64/**", "anon");
	    chainDefinition.addPathDefinition("/user/doLogin", "anon");
		// swagger2 相关资源放行 用anon  具体的资源路径在 springfox-swagger2-ui-2.9.2.jar包中
		// 接口请求地址 http://localhost:8082/swagger-ui.html
	    chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
	    chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
	    chainDefinition.addPathDefinition("/webjars/**", "anon");
		// swagger api文档路径 http://localhost:8082/v2/api-docs
	    chainDefinition.addPathDefinition("/v2/**", "anon");
	    // 登出 用logout
	    chainDefinition.addPathDefinition("/doLogout", "logout");
	    // 除上面以外的所有路径，跳转到登录页 用user
	    chainDefinition.addPathDefinition("/**", "user");
//	    chainDefinition.addPathDefinition("/doLoginUI", "user");
	    return chainDefinition;
	}
	@Bean
	protected CacheManager shiroCacheManager() {
	    return new MemoryConstrainedCacheManager();
	}
	
	
}
