package com.wanmi.sbc.aop;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListByManageDepartmentIdsRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.elastic.api.request.base.EsBaseQueryRequest;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class PageNumIsolation {

    @Pointcut("@annotation(com.wanmi.sbc.aop.PageNumCheck)")
    public void pageNumCheck() {
    }

    @Before(value = "pageNumCheck()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, IllegalAccessException {
        //获取连接点的方法签名对象
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        //获取到当前执行的方法
        Method method = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        //获取方法的注解
        PageNumCheck annotation = method.getAnnotation(PageNumCheck.class);
        Object[] objects = joinPoint.getArgs();

        for (Object object : objects) {
            if (object instanceof BaseQueryRequest || object instanceof EsBaseQueryRequest) {

                try {
                    Class clazz = object.getClass().getSuperclass();
                    Field field = clazz.getDeclaredField(annotation.pageNum());
                    field.setAccessible(Boolean.TRUE);
                    Integer pageNum = Integer.parseInt(field.get(object).toString());
                    if(pageNum > 99) {
                        throw new SbcRuntimeException("K-910101");
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
