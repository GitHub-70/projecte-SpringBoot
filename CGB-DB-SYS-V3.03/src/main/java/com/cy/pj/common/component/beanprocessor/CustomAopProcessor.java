package com.cy.pj.common.component.beanprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 学习案例：BeanPostProcessor — 通过代理为 Service 添加日志横切关注点
 *
 * 知识点：
 *   BeanPostProcessor 可以在 Bean 初始化后创建代理对象，实现类似 AOP 的横切关注点注入。
 *   本案例为 @Service Bean 创建 JDK Proxy，在方法调用前后打印日志。
 *   这展示了 AOP 代理的核心原理，但实际开发应使用 @Aspect + @Around（项目已有 SysLogAspect）。
 *
 *   JDK Proxy vs CGLIB：
 *   - JDK Proxy：只代理接口方法，要求目标类实现接口
 *   - CGLIB：代理类的所有方法（包括非接口方法），Spring AOP 默认使用
 *   - 项目使用 @EnableAutoConfiguration + spring.aop.proxy-target-class=true，默认 CGLIB
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. 项目已有 SysLogAspect（@Aspect）处理 @RequiredLog 注解
 *   2. 手动代理会与 Spring AOP 创建的 CGLIB 代理冲突（双重代理）
 *   3. JDK Proxy 只代理接口方法，对于未实现接口的 Service 会跳过
 */
public class CustomAopProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CustomAopProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 只为 @Service 且实现了接口的 Bean 创建代理，所有JDK代理创建前增加了 getInterfaces().length > 0 检查，避免无接口类崩溃
        if (isServiceBean(bean) && bean.getClass().getInterfaces().length > 0) {
            return createLoggingProxy(bean, beanName);
        }
        return bean;
    }

    private boolean isServiceBean(Object bean) {
        return bean.getClass().isAnnotationPresent(Service.class);
    }

    private Object createLoggingProxy(Object bean, String beanName) {
        return Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                new LoggingInvocationHandler(bean, beanName)
        );
    }

    /**
     * 日志调用处理器 — 在方法调用前后打印日志
     * 对比项目的 SysLogAspect（@Aspect + @Around），后者更优雅且支持 CGLIB
     */
    private static class LoggingInvocationHandler implements InvocationHandler {
        private final Object target;
        private final String beanName;
        private final Logger logger = LoggerFactory.getLogger("AopProxyLogger");

        LoggingInvocationHandler(Object target, String beanName) {
            this.target = target;
            this.beanName = beanName;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.info("[{}] Before: {}.{}", beanName, target.getClass().getSimpleName(), method.getName());
            try {
                Object result = method.invoke(target, args);
                logger.info("[{}] After: {}.{} → returned", beanName, target.getClass().getSimpleName(), method.getName());
                return result;
            } catch (Exception e) {
                logger.error("[{}] Exception in {}.{}: {}", beanName, target.getClass().getSimpleName(), method.getName(), e.getMessage());
                throw e;
            }
        }
    }
}
