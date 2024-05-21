package com.cy.pj.common.selector;

import com.cy.pj.common.annotation.AppUserDefinedConfig;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自定义导入选择器
 *     通过实现ImportSelector接口，可以编写一个选择器类来动态选择要导入的配置类。
 *     选择器类可以根据特定的条件来选择不同的配置类进行导入。
 *
 *     实现另外两个接口，为了模拟 AutoConfigurationImportSelector 的实现
 */
public class MyImportSelector implements ImportSelector, EnvironmentAware, Ordered {

    private Environment environment;

    private static final String[] NO_IMPORTS = {};

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected final Environment getEnvironment() {
        return this.environment;
    }

    /**
     * 把返回的字符串数组的类型注入到容器中
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        if (!isEnabled(importingClassMetadata)) {
            return NO_IMPORTS;
        }
        // 根据条件选择要导入的配置类
        return new String[]{"SpringWebConfig", "CorsConfig"};
    }

    /**
     * 首先，它检查当前类是否等于MyImportSelector类。
     *      如果是，则通过getEnvironment().getProperty()方法获取一个属性值，
     *      该属性值用于覆盖是否启用自动配置的决定。该属性的默认值为true。
     *      如果当前类不等于MyImportSelector类，则直接返回true
     * @param metadata
     * @return
     */
    protected boolean isEnabled(AnnotationMetadata metadata) {
        if (getClass() == MyImportSelector.class) {
//            return getEnvironment().getProperty(EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true);
            return getEnvironment().getProperty(AppUserDefinedConfig.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true);
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}