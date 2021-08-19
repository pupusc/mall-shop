package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 0：顶层类的父节点
 * Created by zhangjin on 2017/3/22.
 */
@ApiEnum
public enum CateParentTop {
    @ApiEnumProperty("0：顶层类的父节点")
    ZERO;
    @JsonCreator
    public CateParentTop fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
