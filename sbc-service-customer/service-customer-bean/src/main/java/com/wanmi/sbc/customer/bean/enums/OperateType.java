package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 操作类型 0:扣除 1:增长'
 *
 * @author yang
 * @since 2019/2/22
 */
@ApiEnum
public enum OperateType {
    @ApiEnumProperty("扣除")
    DEDUCT,
    @ApiEnumProperty("增长")
    GROWTH;


    @JsonCreator
    public OperateType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
