package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum GoodsAdAuditStatus {
    @ApiEnumProperty("0：待提交同盾审核")
    WAIT,

    @ApiEnumProperty("1: 已提交审核但还没有到同盾")
    WAITTOAUDIT,

    @ApiEnumProperty("2：已提交同盾审核但未返回结果")
    AUDITING,

    @ApiEnumProperty("3：同盾审核成功")
    SUCCESS,

    @ApiEnumProperty("4：同盾审核失败")
    FAIL;

    @JsonCreator
    public static GoodsAdAuditStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
