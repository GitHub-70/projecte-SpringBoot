package com.cy.pj.common.component.beanprocessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

/**
 * 学习案例：BeanPostProcessor — Micrometer 方法耗时监控代理
 *
 * 知识点：
 *   通过 BeanPostProcessor 为 Service Bean 创建监控代理，使用 Micrometer Timer
 *   记录每个方法的执行耗时。这是"无侵入监控"的一种实现思路。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. JDK Proxy 只对实现了接口的 Service 生效，未实现接口的会被跳过
 *   2. 与 Spring AOP（@Transactional 等）的 CGLIB 代理冲突，可能产生双重代理
 *   3. 对所有 Service 统一创建代理过于激进，应按需监控而非全量监控
 *   4. Micrometer 推荐使用 @Timed 注解 + AOP 方式，更精准且不冲突
 *
 *   Micrometer 推荐用法：
 *   @Timed(value = "sys_user.findPageObjects", description = "查询用户耗时")
 *   public PageObject findPageObjects(...) { ... }
 *
 * 如需测试：临时加上 @Component（建议同时排除特定 Service 以避免冲突）
 */
public class MonitoringProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 只为 @Service 且实现了接口的 Bean 创建代理,所有JDK代理创建前增加了 getInterfaces().length > 0 检查，避免无接口类崩溃
        if (isServiceClass(bean.getClass()) && bean.getClass().getInterfaces().length > 0) {
            return Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new MonitoringInvocationHandler(bean, beanName)
            );
        }
        return bean;
    }

    private boolean isServiceClass(Class<?> clazz) {
        // 通过 @Service 注解判断，而非类名后缀，更精准
        return clazz.isAnnotationPresent(Service.class);
    }

    private static class MonitoringInvocationHandler implements InvocationHandler {
        private final Object target;
        private final String beanName;
        private final MeterRegistry meterRegistry;

        public MonitoringInvocationHandler(Object target, String beanName) {
            this.target = target;
            this.beanName = beanName;
            this.meterRegistry = Metrics.globalRegistry;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 使用 Micrometer Timer 记录方法执行耗时
            // Timer.start() → 方法执行 → sample.stop() 自动计算耗时并记录到 MeterRegistry
            Timer.Sample sample = Timer.start(meterRegistry);
            try {
                return method.invoke(target, args);
            } finally {
                // Timer 指标名称使用 bean名.方法名 的格式
                // 如：sysUserServiceImpl.findPageObjects
                sample.stop(Timer.builder("service.method.timer")
                        .tag("bean", beanName)
                        .tag("class", target.getClass().getSimpleName())
                        .tag("method", method.getName())
                        .register(meterRegistry));
            }
        }
    }
}
