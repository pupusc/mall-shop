package com.wanmi.sbc.common.annotation;

import java.lang.annotation.*;

/**
 * 连接优化
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransportMonitor {


}
