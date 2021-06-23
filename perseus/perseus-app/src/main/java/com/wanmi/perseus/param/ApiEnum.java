package com.wanmi.perseus.param;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: songhanlin
 * @Date: Created In 11:34 2019-02-01
 * @Description: ApiEnum注解
 * 因为有的枚举是String, 有的枚举是Integer.
 * 大多数情况下是Integer, 因此默认值设置为Integer, 特殊情况下需要指定为String
 * 例如@ApiEnumProperty(dataType = "java.lang.String")
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface ApiEnum {
    /**
     * @return API description.
     */
    String dataType() default "java.lang.Integer";
}