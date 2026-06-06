
package com.cy.pj.common.component.beanfactory;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 学习案例：BeanFactoryPostProcessor — 根据环境动态修改 BeanDefinition 属性
 *
 * 知识点：
 *   BeanFactoryPostProcessor 在 Bean 实例化之前执行，可以修改 Bean 的配置元数据（属性值、作用域等）。
 *   它操作的是 BeanDefinition（蓝图），而非 Bean 实例本身。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. 项目已通过 Spring Profile 机制（application-local.yml / application-prod.yml）区分环境，
 *      此处理器中的硬编码 URL 会覆盖 YAML 配置，导致冲突。
 *   2. Spring Boot 推荐使用 spring.profiles.active + 多 Profile 配置文件来管理环境差异，
 *      而不是在代码中硬编码。
 *   3. 对于 HikariDataSource，属性名应为 jdbcUrl（而非 url），因为 HikariCP 没有 setUrl() 方法。
 *
 * 如需测试：临时加上 @Component 注解，并确保数据源配置不会被覆盖。
 */
public class EnvironmentAwareProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String env = System.getProperty("spring.profiles.active", "local");

        // 动态替换数据源配置 — 注意：HikariDataSource 使用 jdbcUrl 而非 url
        BeanDefinition dataSourceDef = beanFactory.getBeanDefinition("dataSource");
        MutablePropertyValues properties = dataSourceDef.getPropertyValues();

        if ("prod".equals(env)) {
            properties.add("jdbcUrl", "jdbc:mysql://prod-db:3306/app");
            properties.add("username", "prod_user");
        } else if ("test".equals(env)) {
            properties.add("jdbcUrl", "jdbc:h2:mem:testdb");
            properties.add("username", "sa");
        } else {
            properties.add("jdbcUrl", "jdbc:mysql://localhost:3306/app_dev");
            properties.add("username", "dev_user");
        }
    }
}