package com.cy.pj.common.config;

import com.cy.pj.common.filter.MyFilter;
import com.cy.pj.common.interceptor.TimeAccessInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.Filter;

/**
 * WebMvcConfigurationSupport 是一个抽象类，继承自 WebMvcConfigurer,它提供了一些默认的实现
 *      addViewControllers(): 添加自定义的视图控制器
 *      addResourceHandlers(): 配置静态资源的处理
 *      addInterceptors(): 定义自定义的拦截器
 *      configureDefaultServletHandling(): 配置默认的 Servlet 处理
 */
@Component
public class SwaggerConfig extends WebMvcConfigurationSupport {


    // todo 监听器

    // 注册过滤器
    @Bean
    public FilterRegistrationBean<Filter> repeatedlyReadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter myFilter = new MyFilter();
        registration.setFilter(myFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }


    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器
        registry.addInterceptor(new TimeAccessInterceptor())
                .addPathPatterns("/user/doLogin");
//                .addPathPatterns("/**");
    }

    /**
     * 配置Spring MVC中的静态资源处理器。
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将所有请求路径都映射到classpath下的/static/目录中
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        // 配置了Swagger的UI界面资源路径。
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");

        // 调用父类方法，进行额外的资源处理器配置。
        super.addResourceHandlers(registry);
    }

    /**
     * 配置Spring MVC中的视图控制器。
     * 访问 /swagger-ui.html 转发到 /webjars/springfox-swagger-ui/index.html
     * @param registry
     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/swagger-ui.html").setViewName("forward:/webjars/springfox-swagger-ui/index.html?url=/v2/api-docs");
//    }

}