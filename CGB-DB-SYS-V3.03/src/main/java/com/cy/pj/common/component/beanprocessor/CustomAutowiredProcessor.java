package com.cy.pj.common.component.beanprocessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

/**
 * 学习案例：BeanPostProcessor — 自定义依赖注入与初始化回调
 *
 * 知识点：
 *   BeanPostProcessor.postProcessBeforeInitialization() 在 @PostConstruct 之前执行，
 *   可以在此阶段实现自定义的依赖注入（如按自定义注解注入）和初始化检查。
 *
 *   Spring 内置的 BeanPostProcessor 实现了同样的功能：
 *   - AutowiredAnnotationBeanPostProcessor：处理 @Autowired、@Value、@Inject
 *   - CommonAnnotationBeanPostProcessor：处理 @PostConstruct、@PreDestroy、@Resource
 *   - InitDestroyAnnotationBeanPostProcessor：处理 @PostConstruct/@PreDestroy 生命周期
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   Spring 已自动注册上述内置处理器，手动实现会与它们冲突。
 *   学习此案例是为了理解 Spring 依赖注入的底层机制，而非替代它。
 */
public class CustomAutowiredProcessor implements BeanPostProcessor {

    private ApplicationContext applicationContext;

    /**
     * 注入 ApplicationContext 以获取其他 Bean 实例
     * （实际 Spring 通过 Aware 回调注入，这里简化为 setter）
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (applicationContext == null) {
            return bean;
        }

        Class<?> clazz = bean.getClass();

        // 自定义字段注入：遍历所有字段，按类型从容器中获取依赖
        // Spring 的 AutowiredAnnotationBeanPostProcessor 只处理标注了 @Autowired 的字段，
        // 此示例展示"无注解自动注入"（按类型匹配）——仅供学习，不应在生产中使用
        for (Field field : clazz.getDeclaredFields()) {
            // 排除 static 和 final 字段
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            // 尝试从容器中获取该类型的 Bean
            try {
                Object dependency = applicationContext.getBean(field.getType());
                field.setAccessible(true);
                field.set(bean, dependency);
            } catch (Exception e) {
                // 容器中没有该类型 Bean 或注入失败，跳过此字段
                // Spring 的 @Autowired(required=false) 同样是可选注入
            }
        }

        // 自定义初始化回调：查找名为 "init" 的无参方法并调用
        // Spring 的 CommonAnnotationBeanPostProcessor 处理 @PostConstruct 注解方法
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("init") && method.getParameterCount() == 0) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke init method on bean: " + beanName, e);
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
