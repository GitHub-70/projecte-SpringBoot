package com.cy.pj.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听器
 *   ServletContext域中：
 *      ServletContextListener
 *          何时创建：服务器启动创建
 *          何时销毁：服务器关闭销毁
 *      ServletContextAttributeListener
 *
 *   HttpSession域中：
 *      HttpSessionListener
 *          何时创建：第一次调用request.getSession时创建
 *          何时销毁：服务器关闭销毁 session过期 手动销毁
 *          场景：记录用户访问网站的人数，每切换一个浏览器都会记录用户访问次数
 *      HttpSessionAttributeListener
 *
 *   ServletRequest域中：
 *      ServletRequestListener
 *          何时创建：每一次请求都会创建request
 *          何时销毁：请求结束
 *      ServletRequestAttributeListener
 *
 *   对象感知监听器:（主体是针对一个对象且不需要在web.xml中配置）
 *      HttpSessionBindingListener
 *      HttpSessionActivationListener
 */
@Component
public class MySessionListener implements HttpSessionListener {

    private Logger logger = LoggerFactory.getLogger(MySessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        logger.info("Session created!");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("Session destroyed!");
    }
}
