package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import lombok.NoArgsConstructor;

/**
 * 限售的方式 : 0-按人  1-按订单
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
@NoArgsConstructor
public enum RestrictedType {

    @ApiEnumProperty("0：按人")
    RESTRICTED_HUMAN,

    @ApiEnumProperty("1：按订单")
    RESTRICTED_ORDER;

    private Integer type;

    private String desc;

    RestrictedType(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }

    @JsonCreator
    public static RestrictedType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
