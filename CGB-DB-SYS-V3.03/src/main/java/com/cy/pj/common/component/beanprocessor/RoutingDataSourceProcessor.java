package com.cy.pj.common.component.beanprocessor;

import com.cy.pj.common.annotation.ReadOnly;
import com.cy.pj.common.component.datasource.DataSourceContextHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Proxy;

/**
 * 学习案例：BeanPostProcessor — 读写分离数据源路由代理
 *
 * 知识点：
 *   BeanPostProcessor.postProcessAfterInitialization() 可以在 Bean 初始化后替换其实例。
 *   本案例为 @Repository Bean 创建 JDK 动态代理，根据方法上的 @ReadOnly 注解
 *   在执行前设置 DataSourceContextHolder 的数据源类型（read/write），
 *   配合 DynamicDataSource（AbstractRoutingDataSource）实现读写分离。
 *
 *   JDK Proxy 的限制：只能代理实现了接口的类。如果目标类没有实现任何接口，
 *   getClass().getInterfaces() 返回空数组，Proxy.newProxyInstance() 会抛异常。
 *   解决方案：使用 CGLIB（Spring 默认使用）或检查接口数量后再决定是否代理。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. 项目中的 Dao 层使用 MyBatis Mapper，已经是接口代理，再包装一层可能导致 MyBatis 失效
 *   2. 需配合 DynamicDataSource + 多数据源配置才能生效，当前项目只有单数据源
 *   3. 更推荐使用 AOP（@Aspect + @Around）实现此逻辑，避免与 MyBatis 代理冲突
 *
 * 如需测试：临时加上 @Component，并配置 DynamicDataSource + 读写双数据源。
 */
public class RoutingDataSourceProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (isRepository(bean) && canProxy(bean)) {
            return createRepositoryProxy(bean);
        }
        return bean;
    }

    private boolean isRepository(Object bean) {
        return bean.getClass().isAnnotationPresent(Repository.class) ||
                bean.getClass().getName().toLowerCase().contains("repository") ||
                bean.getClass().getSimpleName().endsWith("Repository");
    }

    /**
     * JDK Proxy 前置检查：目标类必须实现至少一个接口
     * 如果没有接口，应改用 CGLIB 代理（需要引入 spring-boot-starter-aop，项目已有）
     */
    private boolean canProxy(Object bean) {
        return bean.getClass().getInterfaces().length > 0;
    }

    private Object createRepositoryProxy(Object repository) {
        return Proxy.newProxyInstance(
                repository.getClass().getClassLoader(),
                repository.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    // 根据 @ReadOnly 注解选择数据源
                    if (method.isAnnotationPresent(ReadOnly.class)) {
                        DataSourceContextHolder.setDataSourceType("read");
                    } else {
                        DataSourceContextHolder.setDataSourceType("write");
                    }

                    try {
                        return method.invoke(repository, args);
                    } finally {
                        // 必须在 finally 中清除 ThreadLocal
                        // 否则线程池复用时，下一个请求可能继承错误的数据库类型
                        DataSourceContextHolder.clear();
                    }
                }
        );
    }
}
