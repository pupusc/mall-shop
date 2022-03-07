package com.wanmi.sbc.common.handler.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 强制路由到主库
 * @author wanggang
 */
@Aspect
@Component
@Slf4j
public class MasterRouteOnlyAspect {

    @Pointcut("@annotation(com.wanmi.sbc.common.handler.aop.MasterRouteOnly)")
    public void MasterRouteOnlyAspect() {
    }

    /**
     * 强制路由到主库
     * @param joinPoint
     */
    @Before(value = "MasterRouteOnlyAspect()")
    public void before(JoinPoint joinPoint) {
        HintManager.getInstance().setMasterRouteOnly();
        log.info("=======>>设置强制路由<<======");
    }

    /**
     * 解除强制路由
     * @param joinPoint
     */
    @After(value = "MasterRouteOnlyAspect()")
    public void after(JoinPoint joinPoint) {
        HintManager.clear();
        log.info("=======>>解除强制路由<<======");
    }


}
