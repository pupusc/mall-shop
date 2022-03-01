package com.wanmi.sbc.common.handler.aop;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class ClientCallAspect {

    /**
     * 排除掉请求参数的方法名
     */
    private static final List<String> excludedMethods = Arrays.asList("uploadFile");

    @Pointcut("execution(* com.wanmi.sbc..*Provider.*(..))")
    public void pointcutLock() {
    }

    @Around(value = "pointcutLock()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result =  joinPoint.proceed();
        stopWatch.stop();

        log.info("请求微服务模块结束 --> 类名：{} --> 方法名：{}，执行耗时：{}ms",
                targetMethod.getDeclaringClass().getSimpleName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());

        return result;
    }

    @Before("pointcutLock()")
    public void before(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = methodSignature.getMethod();

        log.info("请求微服务模块开始 --> 类名：{} --> 方法名：{}，请求参数：{}",
                targetMethod.getDeclaringClass().getSimpleName(), point.getSignature().getName(), getMessage(point));
    }

    @AfterReturning(pointcut = "pointcutLock()", returning = "res")
    public void after(Object res) throws SbcRuntimeException {
        if (!(res instanceof BaseResponse)) {
            return;
        }
        if (CommonErrorCode.SUCCESSFUL.equals(((BaseResponse) res).getCode())) {
            return;
        }
        String errCode = ((BaseResponse) res).getCode();
        String errMsg = ((BaseResponse) res).getMessage();
        Object context = ((BaseResponse) res).getErrorData();
        if (context == null) {
            throw new SbcRuntimeException(errCode, errMsg);
        }
        if (StringUtils.isEmpty(errMsg)){
            throw new SbcRuntimeException(context, errCode);
        }
        throw new SbcRuntimeException(context, errCode, errMsg);
    }

    /**
     * 获取异常信息
     */
    private String getMessage(JoinPoint point) {
        String message = "业务特殊处理, 忽略请求参数!";
        if (!excludedMethods.contains(point.getSignature().getName())) {
            message = JSONObject.toJSONString(point.getArgs());
        }
        return message;
    }
}
