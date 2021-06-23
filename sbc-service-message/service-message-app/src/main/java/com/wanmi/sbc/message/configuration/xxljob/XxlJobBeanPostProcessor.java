package com.wanmi.sbc.message.configuration.xxljob;

import com.xxl.job.core.handler.IJobHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;


/**
 * \* Author: zgl
 * \* Date: 2020-2-6
 * \* Time: 10:24
 * \* Description:
 * \
 */
@Component
@Slf4j
public class XxlJobBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    @Setter
    private BeanFactory beanFactory;

/*
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
*/

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /**
         * 不能直接用XxjobHnadlerTraceWrapper进行包装，返出去的类型不匹配。只能通过代理方式
         */
        return bean instanceof IJobHandler ? wrap(bean) : bean;
    }


    private Object wrap(Object bean) {
        boolean classFinal = Modifier.isFinal(bean.getClass().getModifiers());
        boolean cglibProxy = !classFinal;
        IJobHandler job = (IJobHandler) bean;
        try {
            return createProxy(bean, cglibProxy, new XxlJobMethodInterceptor<IJobHandler>(job, this.beanFactory));
        } catch (AopConfigException ex) {
            if (cglibProxy) {
                if (log.isDebugEnabled()) {
                    log.debug("Exception occurred while trying to create a proxy, falling back to JDK proxy", ex);
                }
                return createProxy(bean, false, new XxlJobMethodInterceptor<IJobHandler>(job, this.beanFactory));
            }
            throw ex;
        }
    }

    Object createProxy(Object bean, boolean cglibProxy, Advice advice) {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setProxyTargetClass(cglibProxy);
        factory.addAdvice(advice);
        factory.setTarget(bean);
        return factory.getObject();
    }

}
