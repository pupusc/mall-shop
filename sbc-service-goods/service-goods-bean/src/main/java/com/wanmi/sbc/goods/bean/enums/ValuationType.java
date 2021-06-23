package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 计价方式
 * 0:按件数,1:按重量,2:按体积
 * Created by sunkun on 2018/5/2.
 */
@ApiEnum
public enum ValuationType {

    @ApiEnumProperty("0:按件数")
    NUMBER,

    @ApiEnumProperty("1:按重量")
    WEIGHT,

    @ApiEnumProperty("2:按体积")
    VOLUME;

    @JsonCreator
    public ValuationType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
