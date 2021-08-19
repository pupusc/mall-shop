package com.wanmi.sbc.message.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-4
 * \* Time: 14:45
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \ 用于对bean的操作
 */
@Component
public class ApplicationConfig implements ApplicationContextAware {
    public static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(this.CONTEXT == null){
            CONTEXT =  applicationContext;
        }
    }
}
