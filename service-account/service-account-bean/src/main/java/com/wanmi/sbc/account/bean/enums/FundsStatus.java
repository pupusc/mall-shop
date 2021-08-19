package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;


/**
 * 会员资金明细-资金状态 0: 未入账 1:成功入账
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:31
 * @version: 1.0
 */
@ApiEnum
public enum FundsStatus {

    @ApiEnumProperty("未入账")
    NO,

    @ApiEnumProperty("成功入账")
    YES;

    @JsonCreator
    public FundsStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
