package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * tab类型
 * Created by of2975 on 2019/4/26.
 */
@ApiEnum(dataType = "java.lang.String")
public enum TabType {
    /**
     *  0 全部
     */
    @ApiEnumProperty("全部")
    ALL("全部"),
    /**
     *  1 收入
     */
    @ApiEnumProperty("收入")
    INCOME("收入"),

    /**
     *  2 支出
     */
    @ApiEnumProperty("支出")
    EXPEND("支出"),

    /**
     *  3 分销员佣金
     */
    @ApiEnumProperty("分销员佣金")
    COMMISSION("分销员佣金"),

    /**
     * 佣金提成
     */
    @ApiEnumProperty("佣金提成")
    COMMISSION_COMMISSION("佣金提成");

    private String desc;

    TabType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static TabType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
