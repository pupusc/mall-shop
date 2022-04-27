package com.soybean.mall.common.spring.filter;

import com.soybean.mall.common.spring.manager.TraceIdManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Liang Jun
 * @date 2022-04-27 11:12:00
 */
@Aspect
@Component
public class TraceJobAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("execution(public com.xxl.job.core.handler.IJobHandler.execute(String))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = TraceIdManager.createTraceId();
        log.info("xxl-job->execute");

        return joinPoint.proceed();
    }
}
