package com.wanmi.sbc.message.bean.enums.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 账户通知枚举类
 */
@ApiEnum
public enum CustomerManageType {
    @ApiEnumProperty("用户新增通知")
    CUSTOMER_PASSWORD("CUSTOMER_PASSWORD", "用户新增通知"),

    @ApiEnumProperty("员工密码短信通知")
    EMPLOYEE_PASSWORD("EMPLOYEE_PASSWORD","员工密码短信通知"),

    @ApiEnumProperty("会员导入成功信息通知")
    CUSTOMER_IMPORT_SUCCESS("CUSTOMER_IMPORT_SUCCESS", "会员导入成功信息通知");

    private String type;

    private String description;

    CustomerManageType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @JsonCreator
    public CustomerManageType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
