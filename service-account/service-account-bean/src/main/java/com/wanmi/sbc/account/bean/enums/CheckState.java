package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * tab状态 checkState
 * Created by of2975 on 2019/4/22.
 */
@ApiEnum(dataType = "java.lang.String")
public enum CheckState {

    /**
     * 待审核 0
     */
    @ApiEnumProperty("待审核")
    CHECK("待审核"),

    /**
     * 已完成 1
     */
    @ApiEnumProperty("已完成")
    FINISH("已完成"),

    /**
     * 提现失败 2
     */
    @ApiEnumProperty("提现失败")
    FAIL("提现失败"),

    /**
     * 审核未通过 3
     */
    @ApiEnumProperty("审核未通过")
    NOT_AUDIT("审核未通过"),

    /**
     * 已取消 4
     */
    @ApiEnumProperty("已取消")
    CANCEL("已取消");

    private String desc;

    CheckState(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public CheckState fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
