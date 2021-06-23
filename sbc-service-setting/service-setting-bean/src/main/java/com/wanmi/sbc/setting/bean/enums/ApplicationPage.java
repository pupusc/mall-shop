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
public enum ApplicationPage {

    @ApiEnumProperty("0：商城首页")
    DISABLE,
    @ApiEnumProperty("1：购物车")
    ENABLE;

    @JsonCreator
    public ApplicationPage fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
