package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum DimensionName {
    @ApiEnumProperty("访问")
    ACCESS,

    @ApiEnumProperty("加购")
    ADD_CAR,

    @ApiEnumProperty("收藏")
   FOLLOW_GOODS,

    @ApiEnumProperty("下单")
    ADD_ORDER,

    @ApiEnumProperty("付款")
    PAY_ORDER,

    @ApiEnumProperty("申请退单")
    RETURN_ORDER,

    @ApiEnumProperty("评价商品")
    GOODS_EVALUATE,

    @ApiEnumProperty("评价店铺")
    STORE_EVALUATE,

    @ApiEnumProperty("关注店铺")
    FOLLOW_STORE,

    @ApiEnumProperty("分享商品")
    SHARE_GOODS,

    @ApiEnumProperty("分享商城")
    SHARE_S2B,

    @ApiEnumProperty("分享店铺")
    SHARE_STORE,

    @ApiEnumProperty("邀请好友")
    INVITE,

    @ApiEnumProperty("签到")
    SIGN_IN,

    @ApiEnumProperty("分享赚")
    COMMISSION;

    @JsonCreator
    public static DimensionName fromValue(int name) {
        return values()[name];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
