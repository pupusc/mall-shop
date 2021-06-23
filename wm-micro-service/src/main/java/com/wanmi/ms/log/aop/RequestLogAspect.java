package com.wanmi.ms.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * RequestLog切面, 与RequestLogFilter相互配合, 让数据记录更完整
 * Created by aqlu on 16/7/29.
 */
@Aspect
@Slf4j
public class RequestLogAspect {

    @Around(value = "(within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)) && @annotation(requestMapping)")
    public Object logAround(ProceedingJoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {
        String clzMethod = null;
        try {
            Signature signature = joinPoint.getSignature();
            clzMethod = new StringJoiner(".").add(signature.getDeclaringTypeName()).add(signature.getName()).toString();
            log.debug("{}, {}, {} in", clzMethod);

            String[] path = requestMapping.path();
            path = (path.length == 0) ? requestMapping.value() : path;
            String mapping = Arrays.stream(path).collect(Collectors.joining(",", "[", "]"));

            RequestMethod[] mappingMethods = requestMapping.method();
            String mappingMethodsStr = Arrays.stream(mappingMethods).map(RequestMethod::toString).collect(Collectors.joining(",", "[", "]"));

            MDC.put("reqLog_clzMethod", clzMethod);
            MDC.put("reqLog_mapping", String.format("%s %s", mappingMethodsStr, mapping));
            MDC.put("reqLog_mappingName", requestMapping.name()); // 记录接口名, 便于问题定位

            return joinPoint.proceed();
        } catch (Throwable e) {
            String exMsg = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
            MDC.put("reqLog_exMsg", exMsg);

            log.debug("{}发生异常, exMsg: {}", clzMethod, exMsg);
            throw e;
        }
    }
}
