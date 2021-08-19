package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @author: lq
 * @CreateTime:2019-02-11 16:06
 * @Description:todo
 */
@ApiEnum
public enum GoodsShowType {
    @ApiEnumProperty("0:SKU")
    SKU,
    @ApiEnumProperty("1:SPU")
    SPU;

    @JsonCreator
    public GoodsShowType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
