package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 弹窗管理-应用页管理
 *  2020/4/21
 */
@ApiEnum
public enum UsePageType {

    @ApiEnumProperty("0：店铺首页")
    STORE_INDEX,
    @ApiEnumProperty("1：商品列表")
    GOODS_LIST,
    @ApiEnumProperty("2：商品详情")
    GOODS_DETAIL,
    @ApiEnumProperty("3：专题页")
    SPECIAL_PAGE;



    @JsonCreator
    public static UsePageType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
