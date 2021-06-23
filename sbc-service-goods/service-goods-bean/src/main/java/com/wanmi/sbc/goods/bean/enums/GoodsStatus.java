package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 商品状态 0：正常 1：缺货 2：失效
 * Created by Daiyitian on 2017/4/13.
 */
@ApiEnum
public enum GoodsStatus {

    @ApiEnumProperty(" 0：正常")
    OK,

    @ApiEnumProperty("1：缺货")
    OUT_STOCK,

    @ApiEnumProperty("2：失效")
    INVALID,

    @ApiEnumProperty("3：无购买权限")
    NO_AUTH;

    @JsonCreator
    public GoodsStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
