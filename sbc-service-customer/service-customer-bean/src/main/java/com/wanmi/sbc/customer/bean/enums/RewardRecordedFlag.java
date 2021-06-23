package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 奖励是否入账 0：否 1：是
 * Created by of2975 on 2019/4/26.
 */
@ApiEnum(dataType = "java.lang.String")
public enum RewardRecordedFlag {
    /**
     *  0 未入账
     */
      @ApiEnumProperty("未入账")
      NO("未入账"),
    /**
     *  1 已入账
     */
      @ApiEnumProperty("已入账")
      YES("已入账");

    private String desc;

    RewardRecordedFlag(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public RewardRecordedFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
