package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>平台类型</p>
 * Created by of628-wenzhi on 2017-05-23-下午3:39.
 */
@ApiEnum(dataType = "java.lang.String")
public enum Platform {

    /**
     * BOSS
     */
    @ApiEnumProperty("BOSS")
    BOSS("商户"),

    /**
     * 商户(小B)
     */
    @ApiEnumProperty("商户(小B)")
    CUSTOMER("客户"),

    /**
     * 第三方
     */
    @ApiEnumProperty("第三方")
    THIRD("第三方"),

    /**
     * 商家
     */
    @ApiEnumProperty("商家")
    SUPPLIER("商家"),

    /**
     * 平台
     */
    @ApiEnumProperty("平台")
    PLATFORM("平台"),

    /**
     * 品牌商
     */
    @ApiEnumProperty("品牌商")
    MALL("品牌商"),

    /**
     * 供应商
     */
    @ApiEnumProperty("供应商")
    PROVIDER("供应商");

    private String desc;

    Platform(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static Platform forValue(String name) {
        return Platform.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
