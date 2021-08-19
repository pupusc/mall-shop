package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 完成状态 提现单完成状态(0:未完成,1:已完成)
 * @author chenyufei
 */
@ApiEnum
public enum FinishStatus {

    /**
     * 未完成
     */
    @ApiEnumProperty("未完成")
    UNSUCCESS,

    /**
     * 已完成
     */
    @ApiEnumProperty("已完成")
    SUCCESS;

    @JsonCreator
    public static FinishStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
