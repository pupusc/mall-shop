package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:28 AM 2018/9/12
 * @Description: 购满类型
 */
@ApiEnum
public enum FullBuyType {

    /**
     * 无门槛
     */
    @ApiEnumProperty("0：无门槛")
    NO_THRESHOLD(0),

    /**
     * 满N元可使用
     */
    @ApiEnumProperty("1：满N元可使用")
    FULL_MONEY(1);

    private int type;

    FullBuyType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static FullBuyType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }
}
