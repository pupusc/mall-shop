package com.wanmi.sbc.common.annotation;

import java.lang.annotation.*;

/**
 * Created by sunkun on 2017/7/26.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsCacheAnnotation {
    String name();
}
