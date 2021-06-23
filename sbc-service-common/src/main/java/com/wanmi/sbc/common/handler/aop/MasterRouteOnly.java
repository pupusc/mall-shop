package com.wanmi.sbc.common.handler.aop;

import java.lang.annotation.*;

/**
 * 强制路由到主库
 * @author wanggang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MasterRouteOnly {

}
