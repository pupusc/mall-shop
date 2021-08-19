package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 参加营销商品类型范围
 */
@ApiEnum
public enum MarketingScopeType {
    /**
     * 所有货品
     */
    @ApiEnumProperty("0：所有货品")
    SCOPE_TYPE_ALL(0),

    /**
     * 自定义货品
     */
    @ApiEnumProperty("1：自定义货品")
    SCOPE_TYPE_CUSTOM(1);

    private int type;

    MarketingScopeType(int type){
        this.type = type;
    }

    @JsonCreator
    public MarketingScopeType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }
}
