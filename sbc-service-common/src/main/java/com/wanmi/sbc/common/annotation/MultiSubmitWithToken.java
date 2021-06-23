package com.wanmi.sbc.common.annotation;

import java.lang.annotation.*;

/**
 * 根据token维度的防抖操作,不支持多个终端防抖，仅支持同一终端防抖
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiSubmitWithToken {
}
