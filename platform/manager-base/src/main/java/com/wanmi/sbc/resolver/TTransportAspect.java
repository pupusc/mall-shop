package com.wanmi.sbc.resolver;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TTransportAspect {

    @Pointcut("@annotation(com.wanmi.sbc.common.annotation.TransportMonitor)")
    public void annotationPointCut() {
    }

    @Before("annotationPointCut()")
    public void doBefore(JoinPoint joinPoint) {
    }
}
