
package com.cy.pj.common.component.beanfactory;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

/**
 * 学习案例：BeanFactoryPostProcessor — 占位符解析
 *
 * 知识点：
 *   BeanFactoryPostProcessor 可以在 Bean 实例化前，遍历所有 BeanDefinition 的属性值，
 *   将 ${...} 占位符替换为实际值。Spring 内置的 PropertySourcesPlaceholderConfigurer
 *   就是通过此机制实现 @Value("${xxx}") 和 XML 中占位符的自动解析。
 *
 * ⚠️ 本案例已被禁用（无 @Component），因为：
 *   Spring Boot 自动注册了 PropertySourcesPlaceholderConfigurer，已经完整处理了占位符。
 *   手动再实现一个会造成重复处理，且此实现无法感知 Spring Environment 中的属性源层级。
 *
 * 如需测试：临时加上 @Component，并在某个 Bean 的 XML/注解配置中使用 ${placeholder} 属性。
 */
public class CustomPropertyProcessor implements BeanFactoryPostProcessor {

    private Environment environment;

    /**
     * 可以通过 setter 注入 Environment，获取 Spring 的完整属性源链
     * （包括系统属性、环境变量、application.yml 等）
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                Object value = propertyValue.getValue();
                if (value instanceof String) {
                    String resolvedValue = resolvePlaceholder((String) value);
                    if (!resolvedValue.equals(value)) {
                        propertyValues.addPropertyValue(propertyValue.getName(), resolvedValue);
                    }
                }
            }
        }
    }

    /**
     * 占位符解析：将 ${key} 替换为 Environment 中对应的值
     *
     * Spring 的 PropertySourcesPlaceholderConfigurer 使用更复杂的逻辑：
     *   1. 支持嵌套占位符 ${${innerKey}}
     *   2. 支持默认值 ${key:defaultValue}
     *   3. 支持类型转换（String → int/boolean 等）
     */
    private String resolvePlaceholder(String value) {
        if (environment == null || value == null) {
            return value;
        }
        // 简单实现：匹配 ${...} 模式
        if (value.startsWith("${") && value.endsWith("}")) {
            String key = value.substring(2, value.length() - 1);
            // 支持 ${key:defaultValue} 的默认值语法
            String defaultValue = null;
            int colonIndex = key.indexOf(':');
            if (colonIndex > 0) {
                defaultValue = key.substring(colonIndex + 1);
                key = key.substring(0, colonIndex);
            }
            if (environment.containsProperty(key)) {
                return environment.getProperty(key);
            } else if (defaultValue != null) {
                return defaultValue;
            }
        }
        return value;
    }
}