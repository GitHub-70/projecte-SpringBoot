package com.cy.pj.common.component.beanprocessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * 特性	        BeanFactoryPostProcessor	            BeanPostProcessor
 * 执行时机	    BeanDefinition 加载后，Bean 实例化前	    Bean 实例化后，初始化前后
 * 操作对象	    BeanDefinition（元数据）	                Bean 实例（实际对象）
 * 修改能力	    修改 Bean 的配置元数据	                    修改/包装 Bean 实例
 * 典型应用	    配置处理、Bean 定义修改	                AOP、依赖注入、监控
 * 执行次数	    容器启动时执行一次	                        每个 Bean 初始化时都会执行
 * 可否替换 Bean	不能替换 Bean 实例	                    可以返回完全不同的对象
 * 执行顺序	    先于 BeanPostProcessor	                在 Bean 生命周期中多次执行

 *
 * 1.BeanFactoryPostProcessor 适用场景：
 *      配置文件的处理（加密、占位符）
 *      根据环境动态修改 Bean 配置
 *      注册基于条件的 Bean 定义
 *      系统级别的配置修改
 *
 * 2.BeanPostProcessor 适用场景：
 *      AOP 代理创建
 *      自定义注解处理
 *      Bean 的监控和增强
 *      接口的动态路由
 *      缓存、事务等横切关注点
 *
 * 注意事项：
 *      避免在 BeanPostProcessor 中注入需要代理的 Bean（可能引起循环依赖）
 *      BeanFactoryPostProcessor 的修改要谨慎，会影响所有 Bean
 *      注意执行顺序，可以通过 Ordered 或 @Order 注解控制
 *      在 postProcessAfterInitialization 中返回代理时，要确保代理正确处理 equals/hashCode 方法
 *      这两个扩展点是 Spring 框架灵活性的关键，合理使用它们可以实现强大的自定义功能，
 *      但同时也要注意它们对性能的影响和潜在的风险。
 *
 *
 * 缓存处理器
 */

/**
 * 学习案例：BeanPostProcessor — 手动缓存代理
 *
 * 知识点：
 *   通过 BeanPostProcessor 为含 @Cacheable 注解的 Bean 创建代理，
 *   在代理中拦截方法调用：先查缓存，命中则直接返回；未命中则执行方法并存入缓存。
 *   这展示了缓存代理的核心原理：AOP + 缓存存储的横切关注点分离。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. Spring Boot 的 CacheAutoConfiguration 已自动处理 @Cacheable/@CacheEvict/@CachePut，
 *      手动再代理会造成双重代理（Spring 代理一层 + 此处理器再代理一层）
 *   2. 项目已有 SysCacheAspect（通过 AOP 处理 @RequiredCache/@ClearCache）
 *   3. 在 BeanPostProcessor 中注入 CacheManager 可能触发过早初始化
 *   4. JDK Proxy 只代理接口方法，遗漏了类本身的非接口方法
 *
 *   学习要点：理解缓存代理原理后，实际开发应使用 Spring Cache（@EnableCaching + @Cacheable）
 *   或项目自定义的 SysCacheAspect，不要手动创建缓存代理。
 *
 * 如需测试：临时加上 @Component（需确保项目未启用 @EnableCaching，避免冲突）
 */
public class CacheProcessor implements BeanPostProcessor {

    // 使用本地 ConcurrentHashMap 作为缓存存储（演示用）
    // Spring Cache 使用 CacheManager（如 CaffeineCacheManager、RedisCacheManager）
    private final Map<String, Object> localCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        // 所有JDK代理创建前增加了 getInterfaces().length > 0 检查，避免无接口类崩溃
        if (hasCacheableMethods(beanClass) && beanClass.getInterfaces().length > 0) {
            return createCacheProxy(bean);
        }

        return bean;
    }

    private boolean hasCacheableMethods(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Cacheable.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成缓存键：方法名 + 参数值拼接
     *
     * Spring Cache 的 @Cacheable 使用 SpEL 表达式生成 key（如 #user.id），
     * 更灵活且可保证唯一性。此处简化为方法名+参数拼接。
     */
    private String generateCacheKey(Method method, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder(method.getDeclaringClass().getSimpleName());
        keyBuilder.append(".").append(method.getName());
        if (args != null) {
            for (Object arg : args) {
                keyBuilder.append(":").append(arg);
            }
        }
        return keyBuilder.toString();
    }

    private Object createCacheProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    Cacheable cacheable = method.getAnnotation(Cacheable.class);
                    if (cacheable != null) {
                        String cacheKey = generateCacheKey(method, args);
                        // 尝试从本地缓存获取
                        Object cachedResult = localCache.get(cacheKey);
                        if (cachedResult != null) {
                            return cachedResult;
                        }
                        // 未命中 → 执行方法并缓存结果
                        Object result = method.invoke(target, args);
                        if (result != null) {
                            localCache.put(cacheKey, result);
                        }
                        return result;
                    }
                    return method.invoke(target, args);
                }
        );
    }
}
