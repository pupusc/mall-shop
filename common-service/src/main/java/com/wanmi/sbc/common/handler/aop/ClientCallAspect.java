package com.wanmi.sbc.common.handler.aop;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
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


@Aspect
@Component
@Slf4j
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
        String str = String.format("请求微服务模块 --> 类名：%s --> 方法名： %s() ",
                targetMethod.getDeclaringClass().getSimpleName(), joinPoint.getSignature().getName());

        StopWatch stopWatch = new StopWatch(str + System.currentTimeMillis());
        stopWatch.start();
        Object result =  joinPoint.proceed();

        stopWatch.stop();

        log.info(str+"执行时间=======>>"+stopWatch.getTotalTimeMillis());

        return result;
    }

    @AfterReturning(pointcut = "pointcutLock()", returning = "res")
    public void after(JoinPoint joinPoint, Object res) throws SbcRuntimeException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        String str = String.format("请求微服务模块 --> 类名：%s --> 方法名： %s() ",
                targetMethod.getDeclaringClass().getSimpleName(), joinPoint.getSignature().getName());

        String requestInfo = DateUtil.nowTime() + str + "请求参数：" + getMessage(joinPoint);

        String errCode = ((BaseResponse) res).getCode();
        String errMsg = ((BaseResponse) res).getMessage();
        Object context = ((BaseResponse) res).getErrorData();
        if (!CommonErrorCode.SUCCESSFUL.equals(errCode)) {
            log.warn(str + "出现异常！请求的接口信息：{}，接口返回信息：{}", requestInfo, res);
            if (context != null) {
                if (StringUtils.isEmpty(errMsg)){
                    throw new SbcRuntimeException(context, errCode);
                }else{
                    throw new SbcRuntimeException(context, errCode, errMsg);
                }
            } else {
                throw new SbcRuntimeException(errCode, errMsg);
            }
        }
        log.info(DateUtil.nowTime(), str + "返回参数：", JSONObject.toJSONString(res));
    }

    @Before("pointcutLock()")
    public void before(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = methodSignature.getMethod();
        String str = String.format("请求微服务模块 --> 类名：%s --> 方法名： %s() ",
                targetMethod.getDeclaringClass().getSimpleName(), point.getSignature().getName());

        String requestInfo = DateUtil.nowTime() + str + "请求参数：" + getMessage(point);
        log.info(requestInfo);
    }

    /**
     * 获取异常信息
     *
     * @param point
     * @return
     */
    private String getMessage(JoinPoint point) {
        String message = "业务特殊处理, 忽略请求参数!";
        if (!excludedMethods.contains(point.getSignature().getName())) {
            message = JSONObject.toJSONString(point.getArgs());
        }
        return message;
    }
}
