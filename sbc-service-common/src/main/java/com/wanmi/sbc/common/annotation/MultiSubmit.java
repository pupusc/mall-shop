package com.wanmi.sbc.common.annotation;

import java.lang.annotation.*;

/**
 * 针对会员维度的防抖注解，支持多个终端防抖
 * @author wanggang
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiSubmit {
}
