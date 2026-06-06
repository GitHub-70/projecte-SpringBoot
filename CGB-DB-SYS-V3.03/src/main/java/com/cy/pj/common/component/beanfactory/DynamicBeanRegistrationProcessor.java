package com.cy.pj.common.component.beanfactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * 学习案例：BeanFactoryPostProcessor — 条件化动态注册 Bean
 *
 * 知识点：
 *   通过 BeanDefinitionRegistry 可以在容器启动阶段动态注册新的 BeanDefinition，
 *   实现"条件化注册"——只有满足特定条件时才将某个类纳入 Spring 管理。
 *
 *   Spring Boot 提供了更优雅的方式实现同样的功能：
 *   @ConditionalOnProperty(prefix = "dynamic", name = "service", havingValue = "true")
 *   @ConditionalOnClass(name = "com.cy.pj.sys.service.SysLogService")
 *   这些注解在 @Configuration 类上使用，无需手动操作 BeanDefinition。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   动态注册的 Bean 必须确保目标类存在且可实例化，否则运行时会抛 ClassNotFoundException。
 *   Spring Boot 的 @Conditional 系列注解已提供了更安全、更声明式的条件注册机制。
 *
 * 如需测试：临时加上 @Component，并设置启动参数 -Dregister.dynamic.service=true
 */
public class DynamicBeanRegistrationProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

            if (shouldRegisterExtraBean()) {
                // 使用 BeanDefinitionBuilder 构建 BeanDefinition，比 GenericBeanDefinition 更安全
                // 它会自动检查类是否存在，并提供链式 API 设置属性
                BeanDefinition beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition("com.cy.pj.sys.service.impl.SysLogServiceImpl")
                        .addPropertyValue("operation", "dynamic")
                        .setScope(BeanDefinition.SCOPE_SINGLETON)
                        .getBeanDefinition();

                // 检查是否已存在同名 BeanDefinition，避免重复注册
                if (!beanFactory.containsBeanDefinition("dynamicLogService")) {
                    registry.registerBeanDefinition("dynamicLogService", beanDefinition);
                }
            }
        }
    }

    private boolean shouldRegisterExtraBean() {
        // 通过系统属性控制是否注册，默认不注册
        // 生产中推荐使用 @ConditionalOnProperty 替代此方式
        return Boolean.parseBoolean(System.getProperty("register.dynamic.service", "false"));
    }
}
