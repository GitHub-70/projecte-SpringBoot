package com.cy.pj.common.annotation;

import com.cy.pj.common.config.CorsConfig;
import com.cy.pj.common.config.SpringWebConfig;
import com.cy.pj.common.selector.MyImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited // 当一个注解被标记为@Inherited时，它可以被子类继承，即子类会自动继承父类上的注解。
@AutoConfigurationPackage // Spring Boot应用程序启动时，Spring Boot会扫描指定的基本包及其子包，以查找所有的@Component、@Service、@Repository等组件，并自动配置它们
@Import({MyImportSelector.class})
public @interface AppUserDefinedConfig {
    // YML的配置信息
    String ENABLED_OVERRIDE_PROPERTY = "tansun.common.appUserDefinedConfig";
}
