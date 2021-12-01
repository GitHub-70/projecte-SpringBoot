package com.cy.pj.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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

	
	
    @Value("${swagger.enable}")
    private Boolean enable;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Api接口")
                .description("Api接口的描述")
                .termsOfServiceUrl("http://www.baidu.com/")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cy.pj.sys.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(enable);
    }
}