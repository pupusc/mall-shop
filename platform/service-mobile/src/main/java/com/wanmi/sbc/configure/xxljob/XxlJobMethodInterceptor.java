package com.wanmi.sbc.configure.xxljob;

import com.xxl.job.core.handler.IJobHandler;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * \* Author: zgl
 * \* Date: 2020-2-6
 * \* Time: 10:33
 * \* Description:
 * \
 */
@AllArgsConstructor
public class XxlJobMethodInterceptor<T extends IJobHandler> implements MethodInterceptor {
    private final T delegate;

    private final BeanFactory beanFactory;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        IJobHandler jobHandler = new XxlJobTraceWrapper(this.beanFactory, this.delegate);
        Method methodOnTracedBean = getMethod(invocation, jobHandler);
        if (methodOnTracedBean != null) {
            try {
                return methodOnTracedBean.invoke(jobHandler, invocation.getArguments());
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                throw (cause != null) ? cause : ex;
            }
        }
        return invocation.proceed();
    }

    private Method getMethod(MethodInvocation invocation, Object object) {
        Method method = invocation.getMethod();
        return ReflectionUtils.findMethod(object.getClass(), method.getName(), method.getParameterTypes());
    }

}