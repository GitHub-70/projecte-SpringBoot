package com.cy.pj.common.selector;

import com.cy.pj.common.annotation.AppUserDefinedConfig;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * 学习案例：ImportSelector — 动态条件化导入配置类
 *
 * ══════════════════════════════════════════════════════════════
 * 【核心知识点】
 * ══════════════════════════════════════════════════════════════
 *
 * 1. ImportSelector 是什么？
 *    ImportSelector 是 Spring 提供的接口，用于【动态选择】要导入的配置类。
 *    它与 @Import 注解配合使用：当 @Import 引用一个 ImportSelector 实现类时，
 *    Spring 会调用其 selectImports() 方法，将返回的类名数组全部导入容器。
 *
 *    对比三种 @Import 方式：
 *    ┌──────────────────────┬──────────────────────────────────────────┐
 *    │ @Import 方式          │ 特点                                    │
 *    ├──────────────────────┼──────────────────────────────────────────┤
 *    │ @Import(类.class)     │ 直接导入一个配置类，静态、无条件           │
 *    │ @Import(Selector)    │ 动态选择，可根据条件返回不同的类名数组      │
 *    │ @Import(Registrar)   │ 动态注册，可手动操作 BeanDefinitionRegistry│
 *    └──────────────────────┴──────────────────────────────────────────┘
 *
 * 2. selectImports() 的返回值规范：
 *    ⚠️ 必须返回【全限定类名】（Fully Qualified Name），如 "com.cy.pj.common.config.SpringWebConfig"
 *    不能返回短类名（如 "SpringWebConfig"），因为 Spring 内部通过 Class.forName() 加载这些类，
 *    短类名会导致 ClassNotFoundException，进而破坏 ConfigurationClassPostProcessor 的处理流程，
 *    最终引发 NoSuchBeanDefinitionException: importRegistry 不可用。
 *
 * 3. 本类实现的三个接口及其作用：
 *    - ImportSelector：核心接口，selectImports() 决定导入哪些类
 *    - EnvironmentAware：感知 Spring Environment，可读取 application.yml 中的属性
 *      （模拟 AutoConfigurationImportSelector 中通过 Environment 判断是否启用）
 *    - Ordered：控制多个 ImportSelector 的执行顺序
 *      （数字越小优先级越高，0 表示最高优先级）
 *
 * 4. 与 Spring Boot 内置 AutoConfigurationImportSelector 的对比：
 *    AutoConfigurationImportSelector 是 Spring Boot 自动配置的核心引擎：
 *    - 它从 spring.factories 文件读取所有 AutoConfiguration 类名
 *    - 通过 @ConditionalOnClass/@ConditionalOnProperty 等条件过滤
 *    - 最终只导入满足条件的自动配置类
 *    本类是其简化版：不做 spring.factories 读取，只做简单的环境属性判断
 *
 * ══════════════════════════════════════════════════════════════
 * 【触发机制】
 * ══════════════════════════════════════════════════════════════
 *
 * 本类通过 @AppUserDefinedConfig 注解的 @Import({MyImportSelector.class}) 元注解绑定。
 * 只有在某个类上标注了 @AppUserDefinedConfig 时，本类才会被 Spring 调用。
 *
 * 触发链路：
 *   类上标注 @AppUserDefinedConfig
 *     → Spring 解析其元注解 @Import({MyImportSelector.class})
 *     → 实例化 MyImportSelector 并调用 setEnvironment() 注入环境
 *     → 调用 selectImports() 获取要导入的配置类全限定名数组
 *     → Spring 将这些类作为 @Configuration 类处理并注册到容器
 *
 * ══════════════════════════════════════════════════════════════
 * 【使用示例】
 * ══════════════════════════════════════════════════════════════
 *
 * 方式一：在启动类上标注 @AppUserDefinedConfig（推荐）
 *
 *   @SpringBootApplication
 *   @AppUserDefinedConfig                          // ← 触发 MyImportSelector
 *   public class Application {
 *       public static void main(String[] args) {
 *           SpringApplication.run(Application.class, args);
 *       }
 *   }
 *
 *   此时 MyImportSelector.selectImports() 被调用，SpringWebConfig 和 SwaggerConfig 被导入。
 *
 * 方式二：通过 YAML 属性禁用导入
 *
 *   # application.yml 中设置：
 *   tansun:
 *     common:
 *       appUserDefinedConfig: false               # ← isEnabled() 返回 false，不导入任何类
 *
 *   此时 selectImports() 返回空数组 NO_IMPORTS，不会导入任何配置类。
 *
 * 方式三：直接使用 @Import（不经过 @AppUserDefinedConfig）
 *
 *   @SpringBootApplication
 *   @Import({MyImportSelector.class})              // ← 直接触发 MyImportSelector
 *   public class Application { ... }
 *
 * ══════════════════════════════════════════════════════════════
 * 【当前项目状态】
 * ══════════════════════════════════════════════════════════════
 *
 * 当前 Application 启动类上没有使用 @AppUserDefinedConfig，因此本 Selector 不会被触发。
 * SpringWebConfig 和 SwaggerConfig 已通过 @ComponentScan 自动扫描并注册（它们分别有
 * @Configuration 和 @Component 注解），所以即使不通过 ImportSelector 导入，
 * 它们也已经存在于容器中。
 *
 * 如需测试 ImportSelector 的效果：在 Application 类上临时加上 @AppUserDefinedConfig，
 * 并在 YAML 中通过 tansun.common.appUserDefinedConfig 属性控制开关。
 */
public class MyImportSelector implements ImportSelector, EnvironmentAware, Ordered {

    private Environment environment;

    /**
     * 空导入数组 — 当 isEnabled() 返回 false 时，不导入任何类
     * 这与 AutoConfigurationImportSelector 的 NO_IMPORTS 常量设计一致
     */
    private static final String[] NO_IMPORTS = {};

    /**
     * EnvironmentAware 回调 — Spring 容器在实例化本类后自动注入 Environment
     *
     * Environment 是 Spring 的环境抽象，包含所有配置源：
     *   - System.getProperties()    → 系统属性
     *   - System.getenv()           → 环境变量
     *   - application.yml/yaml      → 配置文件
     *   - 命令行参数                 → --key=value
     *   它们按优先级从高到低排列，高优先级的会覆盖低优先级的同名属性
     *
     * 通过 Environment，selectImports() 可以根据配置动态决定导入哪些类，
     * 这正是 Spring Boot 自动配置（@ConditionalOnProperty）的核心思想
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 获取 Environment 实例 — 供子类和内部方法使用
     * 设计为 protected final：子类可读取但不可覆盖
     */
    protected final Environment getEnvironment() {
        return this.environment;
    }

    /**
     * ImportSelector 核心方法 — 返回要导入的配置类全限定名数组
     *
     * 执行时机：在 Spring 解析 @Configuration 类的过程中，
     *   ConfigurationClassPostProcessor 发现 @Import 引用了 ImportSelector，
     *   便实例化该 Selector 并调用此方法，将返回的类名全部当作 @Configuration 类处理。
     *
     * ⚠️ 关键约束：
     *   - 返回值必须是全限定类名（FQN），如 "com.cy.pj.common.config.SpringWebConfig"
     *   - 不能返回短类名（如 "SpringWebConfig"），否则 Class.forName() 会抛 ClassNotFoundException
     *   - 不能返回项目中不存在的类名，否则同样会导致启动失败
     *   - 返回空数组（NO_IMPORTS）表示不导入任何类（条件不满足时）
     *
     * @param importingClassMetadata 标注了 @Import 的类的元数据，
     *   包含该类的注解信息、类名等。可用于读取 @AppUserDefinedConfig 的属性值，
     *   实现更精细的条件判断（如根据注解属性选择不同的导入组合）
     * @return 全限定类名数组，每个类名对应一个要导入的 @Configuration 类
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 获取 @AppUserDefinedConfig 的属性值
        if (!isEnabled(importingClassMetadata)) {
            // 条件不满足 → 返回空数组，不导入任何类
            return NO_IMPORTS;
        }
        // 条件满足 → 返回要导入的配置类全限定名数组
        // 这些类会被 Spring 当作 @Configuration 类处理：
        //   1. 解析类上的 @Bean 方法，注册 BeanDefinition
        //   2. 解析类上的其他 @Import，递归处理
        //   3. 解析类上的 @ComponentScan，触发组件扫描
        return new String[]{
                "com.cy.pj.common.config.SpringWebConfig",
                "com.cy.pj.common.config.SwaggerConfig"
        };
    }

    /**
     * 判断 ImportSelector 是否启用
     *
     * 判断逻辑：
     *   1. 如果当前实例就是 MyImportSelector 本身（非子类），
     *      则读取 Environment 中的属性 tansun.common.appUserDefinedConfig，
     *      默认值为 true（即默认启用）
     *   2. 如果当前实例是 MyImportSelector 的子类，
     *      则直接返回 true（子类可能有不同的启用逻辑）
     *
     * 这与 AutoConfigurationImportSelector.isEnabled() 的设计模式一致：
     *   Spring Boot 的 AutoConfigurationImportSelector 也通过 Environment 属性
     *   (spring.boot.enableautoconfiguration) 控制是否启用自动配置
     *
     * 控制方式：
     *   YAML:   tansun.common.appUserDefinedConfig: false
     *   系统属性: -Dtansun.common.appUserDefinedConfig=false
     *   环境变量: TANSUN_COMMON_APPUSERDEFINEDCONFIG=false
     *   （Environment 查找属性时，以上三种方式均有效，优先级：系统属性 > 环境变量 > YAML）
     *
     * @param metadata 标注了触发本 Selector 的注解所在类的元数据
     * @return true=启用导入，false=禁用导入（返回空数组）
     */
    protected boolean isEnabled(AnnotationMetadata metadata) {
        if (getClass() == MyImportSelector.class) {
            // 读取 YAML/系统属性/环境变量中的开关属性
            // AppUserDefinedConfig.ENABLED_OVERRIDE_PROPERTY = "tansun.common.appUserDefinedConfig"
            return getEnvironment().getProperty(AppUserDefinedConfig.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true);
        }
        return true;
    }

    /**
     * Ordered 接口 — 控制 ImportSelector 的执行优先级
     *
     * 当存在多个 ImportSelector 时，Spring 按 getOrder() 返回值从小到大依次调用。
     *   getOrder() = 0     → 最高优先级，最先执行
     *   getOrder() = 1     → 其次
     *   getOrder() = Integer.MAX_VALUE → 最低优先级
     *
     * AutoConfigurationImportSelector.getOrder() 返回的最高优先级是
     * AutoConfigurationImportSelector.DEFAULT_ORDER（默认值 = -2147483648 即 Integer.MIN_VALUE + 1），
     * 确保自动配置在其他 ImportSelector 之前执行。
     *
     * 本类返回 0，优先级较高但低于 Spring Boot 的自动配置。
     * 如需更精细控制，可改为 @Order 注解方式或在子类中覆盖此方法。
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
