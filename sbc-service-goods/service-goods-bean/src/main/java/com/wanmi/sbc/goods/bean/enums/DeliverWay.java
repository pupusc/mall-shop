package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import lombok.NoArgsConstructor;

/**
 * 配送方式
 */
@ApiEnum
@NoArgsConstructor
public enum DeliverWay {

    @ApiEnumProperty("0: 其他")
    OTHER(0, "其他"),

    @ApiEnumProperty("1: 快递")
    EXPRESS(1, "快递");

    private Integer type;

    private String desc;

    DeliverWay(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }

    @JsonCreator
    public static DeliverWay fromValue(int value) {
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
