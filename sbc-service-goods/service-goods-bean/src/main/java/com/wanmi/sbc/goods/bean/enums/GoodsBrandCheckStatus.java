package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:54 2019-02-11
 * @Description: 审核状态(0:未审核,1:通过,2:驳回)
 */
@ApiEnum
public enum GoodsBrandCheckStatus {

    @ApiEnumProperty("0:未审核")
    WAIT_CHECK,

    @ApiEnumProperty("1:通过")
    PASS,

    @ApiEnumProperty("2:驳回")
    NOT_PASS;

    @JsonCreator
    public GoodsBrandCheckStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }


}
