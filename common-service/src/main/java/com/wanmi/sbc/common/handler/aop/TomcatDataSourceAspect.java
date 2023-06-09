package com.wanmi.sbc.common.handler.aop;

//import com.codingapi.txlcn.tc.aspect.weave.DTXResourceWeaver;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
@Aspect
@Slf4j
public class TomcatDataSourceAspect implements Ordered {

    /*@Autowired
    private DTXResourceWeaver dtxResourceWeaver;//TX-LCN 资源切面处理对象

    @Around("execution(public java.sql.Connection org.apache.tomcat.jdbc.pool.DataSourceProxy.getConnection(..) )")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.debug("proxy my aspect..");
        return dtxResourceWeaver.getConnection(() -> (Connection) point.proceed());
    }*/

    @Override
    public int getOrder() {
        return 0;
    }
}
