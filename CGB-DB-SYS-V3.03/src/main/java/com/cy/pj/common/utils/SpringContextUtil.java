package com.cy.pj.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("Injecting apx into springContextUtil:{}", applicationContext);
        if (applicationContext != null) {
            logger.warn("Original apx will be override:{}", applicationContext);
        }

        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取 applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 获取 bean
     * @param requiredType
     * @param <T>
     * @return T
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取 bean
     * @param beanName
     * @param <T>
     * @return T
     */
    public static <T> T getBean(String beanName) {
        assertContextInjected();
        return (T) applicationContext.getBean(beanName);
    }


    /**
     * 判断 applicationContext 是否被注入
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("Appliaction have not been Injected.");
        }
    }
}
