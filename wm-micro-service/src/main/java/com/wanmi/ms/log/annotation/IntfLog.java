package com.wanmi.ms.log.annotation;

import java.lang.annotation.*;

/**
 * 接口日志注解
 * Created by aqlu on 14-9-3.
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntfLog {
    boolean needFullOutputParam() default false;

    boolean needFullInputParam() default false;
}
