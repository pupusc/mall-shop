package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 用户操作状态(0:已申请,1:已取消)
 * @author chenyufei
 */
@ApiEnum
public enum CustomerOperateStatus {

    /**
     * 已申请
     */
    @ApiEnumProperty("已申请")
    APPLY,

    /**
     * 已取消
     */
    @ApiEnumProperty("已取消")
    CANCEL;

    @JsonCreator
    public static CustomerOperateStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
