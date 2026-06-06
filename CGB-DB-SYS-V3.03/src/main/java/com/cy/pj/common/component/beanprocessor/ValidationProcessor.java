package com.cy.pj.common.component.beanprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.InitializingBean;

/**
 * 学习案例：BeanPostProcessor — Bean 初始化验证
 *
 * 知识点：
 *   BeanPostProcessor.postProcessBeforeInitialization() 在 @PostConstruct 和
 *   InitializingBean.afterPropertiesSet() 之前执行，适合在此阶段做 Bean 的前置校验。
 *   postProcessAfterInitialization() 在 Bean 完全初始化后执行，适合做后置检查。
 *
 *   Spring 的 @PostConstruct 注解和 InitializingBean 接口是更常用的初始化回调方式：
 *   - @PostConstruct：声明式，在依赖注入完成后自动调用
 *   - InitializingBean.afterPropertiesSet()：编程式，Spring 内部大量使用
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   对每个 Bean 都做验证检查会增加启动开销，且用 @PostConstruct 在单个 Bean 内
 *   做自检更精准。全局 BeanPostProcessor 验证适合框架级别的统一约束检查。
 */
public class ValidationProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(ValidationProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // 前置验证：对实现了 InitializingBean 的 Bean，检查其必要属性是否已注入
        if (bean instanceof InitializingBean) {
            log.debug("Bean '{}' implements InitializingBean, will validate before afterPropertiesSet()", beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 后置检查：确认 Bean 不处于无效状态
        // 例如：某些 Bean 依赖的配置属性为 null 时应该告警
        log.debug("Bean '{}' initialized successfully, type: {}", beanName, bean.getClass().getSimpleName());
        return bean;
    }
}
