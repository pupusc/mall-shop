package com.wanmi.sbc.crm.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: ZhangLingKe
 * @Description: 统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年
 * @Date: 2019-10-14 16:30
 */
@ApiEnum
public enum Period {

    @ApiEnumProperty(" 0：近一个月")
    ONE_MONTH,

    @ApiEnumProperty("1：近3个月")
    THREE_MONTH,

    @ApiEnumProperty("2：近6个月")
    SIX_MONTH,

    @ApiEnumProperty("3：近一年")
    ONE_YEAR;
    @JsonCreator
    public Period fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getContent(){
        switch (this){
            case ONE_MONTH:
                return "近1个月";
            case THREE_MONTH:
                return "近3个月";
            case SIX_MONTH:
                return "近6个月";
            case ONE_YEAR:
                return "近一年";
        }
        return "";
    }

}
