package com.cy.pj.common.component.beanfactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 学习案例：BeanFactoryPostProcessor — 动态修改 Bean 作用域
 *
 * 知识点：
 *   Bean 的 scope 决定了其生命周期：
 *   - singleton：容器中只有一个实例（默认）
 *   - prototype：每次获取都创建新实例
 *   BeanFactoryPostProcessor 可以在 Bean 实例化前修改其 scope，
 *   例如在测试环境下将 Service 改为 prototype，避免测试间状态共享。
 *
 *   Spring 提供了更简单的方式：直接在类上使用 @Scope("prototype") 注解，
 *   或使用 @Profile + @Scope 组合来实现环境差异化配置。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. getBeanDefinition() 需要精确的 Bean 名称，Spring 默认使用类名首字母小写作为 Bean 名
 *   2. 如果 Bean 名称不存在，会抛出 NoSuchBeanDefinitionException
 *   3. 在测试环境切换 scope 更推荐用 @Scope 注解
 *
 * 如需测试：临时加上 @Component，并设置环境变量 APP_ENV=test
 */
public class ScopeModifyingProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String environment = System.getenv("APP_ENV");

        if ("test".equals(environment)) {
            // 注意：Bean 名称必须与容器中注册的名称一致
            // Spring 默认策略：类名首字母小写 → sysUserServiceImpl（而非 userService）
            try {
                BeanDefinition targetBean = beanFactory.getBeanDefinition("sysUserServiceImpl");
                targetBean.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            } catch (Exception e) {
                // 如果 Bean 不存在，静默跳过而不是抛异常中断启动
                // 生产代码中应使用日志记录此情况
            }
        }
    }
}
