package com.wanmi.sbc.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListCache {

    String prefix() default "";

    Class clazz();

    long seconds();
}
