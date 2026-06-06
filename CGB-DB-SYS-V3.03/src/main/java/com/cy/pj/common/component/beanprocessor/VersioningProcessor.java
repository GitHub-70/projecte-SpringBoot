package com.cy.pj.common.component.beanprocessor;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 学习案例：BeanPostProcessor — API 版本化路由代理
 *
 * 知识点：
 *   通过 BeanPostProcessor + JDK Proxy，可以根据请求头中的版本信息（如 API-Version: 2.0）
 *   在运行时选择不同版本的方法实现。这是"接口多版本共存"的一种思路。
 *
 *   实际开发中更推荐的方式：
 *   1. URI 版本化：/v1/users、/v2/users（最直观，Spring 推荐）
 *   2. Content Negotiation：Accept: application/vnd.company.v2+json
 *   3. 自定义 Header：X-API-Version: 2（本案例使用的方式）
 *   4. Spring 的 @RequestMapping 支持在 Controller 层面直接区分版本
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   1. hasVersionedInterfaces() 仅靠类名判断，过于脆弱
 *   2. RequestContextHolder 在非 Web 环境（异步线程、定时任务）中不可用
 *   3. 版本路由逻辑尚未完善（versionedMethods 永远为空）
 *   4. 实际版本化应在 Controller 层通过 URI 映射实现，而非在 Service 层代理
 *
 * 如需测试：临时加上 @Component，创建一个类名含 "Version" 且实现接口的 Bean
 */
public class VersioningProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 所有JDK代理创建前增加了 getInterfaces().length > 0 检查，避免无接口类崩溃
        if (hasVersionedInterfaces(bean.getClass()) && bean.getClass().getInterfaces().length > 0) {
            return createVersionedProxy(bean);
        }
        return bean;
    }

    /**
     * 检查类是否需要版本路由
     * 改进：不仅检查类名，还检查类上的自定义版本注解（如果有）
     */
    private boolean hasVersionedInterfaces(Class<?> clazz) {
        // 检查类名（简单规则）或类上的注解（更精准）
        return clazz.getSimpleName().contains("Version") ||
               clazz.isAnnotationPresent(ApiVersion.class);
    }

    private Object createVersionedProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new VersioningInvocationHandler(target)
        );
    }

    /**
     * 自定义版本注解 — 标注在类上声明其支持版本路由
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApiVersion {
        String value() default "1.0";
    }

    private static class VersioningInvocationHandler implements InvocationHandler {
        private final Object target;
        private final Map<String, Method> versionedMethods = new HashMap<>();

        VersioningInvocationHandler(Object target) {
            this.target = target;
            collectVersionedMethods(target.getClass());
        }

        /**
         * 收集版本化方法：按方法名 + 版本号建立索引
         * 方法命名约定：findObjects_v1、findObjects_v2
         * 也可以使用自定义注解 @ApiVersionMethod("2.0") 标注每个方法
         */
        private void collectVersionedMethods(Class<?> clazz) {
            for (Method method : clazz.getDeclaredMethods()) {
                String methodName = method.getName();
                // 识别 _v 后缀的版本化方法：methodName_vN 格式
                int versionIndex = methodName.lastIndexOf("_v");
                if (versionIndex > 0) {
                    String baseName = methodName.substring(0, versionIndex);
                    String version = methodName.substring(versionIndex + 2); // "v" 后的部分
                    versionedMethods.put(baseName + "_v" + version, method);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String version = getVersionFromRequest();

            // 查找对应版本的方法：如请求版本 2，查找 findObjects_v2
            String methodKey = method.getName() + "_v" + version;
            Method versionedMethod = versionedMethods.get(methodKey);

            if (versionedMethod != null) {
                versionedMethod.setAccessible(true);
                return versionedMethod.invoke(target, args);
            }

            // 版本方法不存在 → 回退到默认方法
            return method.invoke(target, args);
        }

        /**
         * 安全获取请求中的版本号
         * 注意：RequestContextHolder 只在 Web 请求线程中有效
         */
        private String getVersionFromRequest() {
            try {
                ServletRequestAttributes attributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String apiVersion = request.getHeader("API-Version");
                    if (apiVersion != null && !apiVersion.isEmpty()) {
                        return apiVersion;
                    }
                }
            } catch (Exception e) {
                // 非 Web 环境（异步线程等），回退到默认版本
            }
            return "1.0";
        }
    }
}
