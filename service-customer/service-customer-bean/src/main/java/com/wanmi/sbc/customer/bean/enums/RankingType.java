package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 排行榜类型
 */
@ApiEnum
public enum RankingType {
    @ApiEnumProperty("邀新人数")
    INVITECOUNT,
    @ApiEnumProperty("有效邀新")
    INVITEAVAILABLECOUNT,
    @ApiEnumProperty("销售额")
    SALEAMOUNT,
    @ApiEnumProperty("预估收益")
    COMMISSION;

    @JsonCreator
    public RankingType fromValue(String name) {
        return valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
