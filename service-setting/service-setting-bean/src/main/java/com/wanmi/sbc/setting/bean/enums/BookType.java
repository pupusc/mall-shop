package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 1图书组件 2非图书组件 3视频组件 4广告组件 5指定内容投放
 *
 */
public enum BookType {
    @ApiEnumProperty("1：图书组件")
    BOOK,

    @ApiEnumProperty("2：非图书组件")
    NOT_BOOK,

    @ApiEnumProperty("3：视频组件")
    VIDEO,

    @ApiEnumProperty("4：广告组件")
    ADVERTISEMENT,

    @ApiEnumProperty("5：指定内容投放")
    ASSIGN;

    @JsonCreator
    public static BookType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public Integer toValue() {
        return this.ordinal();
    }

}
