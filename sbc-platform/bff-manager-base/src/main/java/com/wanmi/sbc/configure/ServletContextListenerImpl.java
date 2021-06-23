package com.wanmi.sbc.configure;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-04-24 11:31
 */
@WebListener
public class ServletContextListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("verifyInfo", new ConcurrentHashMap());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
