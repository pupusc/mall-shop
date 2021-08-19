package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 增专资质是否作废 0：否 1：是
 * Created by CHENLI on 2017/4/13.
 */
@ApiEnum(dataType = "java.lang.String")
public enum InvalidFlag {
    /**
     * 否 0
     */
      @ApiEnumProperty("增专资质是否作废 0：否")
      NO("否"),
    /**
     * 是 1
     */
      @ApiEnumProperty("增专资质是否作废 1：是")
      YES("是");

    private String desc;

    InvalidFlag(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    @JsonCreator
    public InvalidFlag fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
