package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 批量调价的执行结果：0 未执行、1 执行成功、2 执行失败
 */
@ApiEnum
public enum PriceAdjustmentResult {

    @ApiEnumProperty("0 未执行")
    UNDO,

    @ApiEnumProperty("1 执行成功")
    DONE,

    @ApiEnumProperty("2 执行失败")
    FAIL;

    @JsonCreator
    public static PriceAdjustmentResult fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
