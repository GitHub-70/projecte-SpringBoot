package com.cy.pj.common.config;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.sys.service.realm.ShiroUserRealm;
/**
 * @Configuration 注解描述的类为spring容器中的配置类,
 * 	此类的实例也会交给spring管理
  *     基于:http://shiro.apache.org/spring-boot.html中,shiro配置说明进行配置.
 */
@Configuration
public class SpringShiroConfig {
	@Bean
	public Realm realm() {
	  return new ShiroUserRealm();
	}
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
	    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
	    // 放行资源 用anon
	    chainDefinition.addPathDefinition("/bower_components/**", "anon");
	    chainDefinition.addPathDefinition("/build/**", "anon");
	    chainDefinition.addPathDefinition("/dist/**", "anon");
	    chainDefinition.addPathDefinition("/plugins/**", "anon");
	    chainDefinition.addPathDefinition("/user/doLogin", "anon");
	    // 登出 用logout
	    chainDefinition.addPathDefinition("/doLogout", "logout");
	    // 跳转到登录页 用user
	    chainDefinition.addPathDefinition("/**", "user");
	    return chainDefinition;
	}
	@Bean
	protected CacheManager shiroCacheManager() {
	    return new MemoryConstrainedCacheManager();
	}
	
	
}
