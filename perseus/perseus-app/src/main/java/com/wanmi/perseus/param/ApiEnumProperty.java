package com.wanmi.perseus.param;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: songhanlin
 * @Date: Created In 11:34 2019-02-01
 * @Description: 之前的ApiEnum更名为ApiEnumProperties
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ApiEnumProperty {
    /**
     * @return API description.
     */
    String value();
}