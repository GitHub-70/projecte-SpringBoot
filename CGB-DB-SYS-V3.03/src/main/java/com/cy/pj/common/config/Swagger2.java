package com.cy.pj.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @ Configuration：spring boot 加载配置。
 * @EnableSwagger2：启用Swagger2。
 * createRestApi()：创建Api的基本信息。RequestHandlerSelectors.basePackage
 * 是通过扫描该路径下的所有Controller定义的API，并产生文档内容（除了被@ApiIgnore指定的请求）。
 * .enable(enable)：通过配置文件控制swagger2是否启用。
 * 在配置文件中配置swagger.enable为“true”或“false”。一般正式环境会关闭swagger功能。
 * 
 * https://www.jianshu.com/p/536506c40815 未完待续
 * 			该配置类Swagger2 是否只能与application 同级？
 * 
 */

@Configuration
@EnableSwagger2
public class Swagger2 {


    /**
     * 一般正式环境会关闭swagger功能
     */
    @Value("${swagger.enable}")
    private Boolean enable;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("管理台接口文档") // 设置文档的标题
                .description("你的应用描述，可以详细描述API的作用和使用方法。") // 设置文档的描述
                .version("1.0.0") // 设置文档的版本信息
                .contact(new Contact("你的名字", "你的网站链接", "你的邮箱")) // 设置联系人信息
                .license("你的许可证") // 设置许可证
                .licenseUrl("许可证的URL") // 设置许可证的URL
                .termsOfServiceUrl("服务条款的URL") // 设置服务条款的URL
                .build();
    }

    @Bean
    public Docket createRestApi() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add(MediaType.APPLICATION_JSON_VALUE);
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("x-client-token",
                        new AuthorizationScope[]{new AuthorizationScope("global","all scope")})))
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select() // 选择需要生成文档的API
                .apis(RequestHandlerSelectors.basePackage("com.cy.pj.sys.controller"))
                .paths(PathSelectors.any())
                .build()
//                .produces(hashSet)
//                .consumes(hashSet)
                // 添加安全认证
                .securitySchemes(Arrays.asList(new ApiKey("x-client-token","x-client-token","header")))
                .securityContexts(Arrays.asList(securityContext))
                .pathMapping("/") // 这里可以设置基础路径，比如"/api"
                .enable(enable);
    }
}