package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 验证类型
 */
@ApiEnum
public enum VerifyType {
    /**
     * 用户注册
     */
    @ApiEnumProperty("用户注册")
    CUSTOMER_REGISTRY,

    /**
     * 用户找回密码
     */
     @ApiEnumProperty("用户找回密码")
    CUSTOMER_REFUND_PASSWORD,

    /**
     * 修改密码
     */
     @ApiEnumProperty("修改密码")
    CUSTOMER_CHANGE_PASSWORD,

    /**
     * 更改手机
     */
     @ApiEnumProperty("更改手机")
    CUSTOMER_CHANGE_PHONE,

    /**
     * 设置支付密码
     */
     @ApiEnumProperty("设置支付密码")
    CUSTOMER_SET_PAY_PASSWORD,

    /**
     * 绑定手机号码
     */
     @ApiEnumProperty("绑定手机号码")
    CUSTOMER_SET_PHONE,

    /**
     * 员工修改密码
     */
     @ApiEnumProperty("员工修改密码")
    EMPLOYEE_CHANGE_PASSWORD,

    /**
     * 用户能否发送短信
     */
     @ApiEnumProperty("用户能否发送短信")
    EMPLOYEE_SEND,

    /**
     * 员工修改手机
     */
     @ApiEnumProperty("员工修改手机")
    EMPLOYEE_CHANGE_PHONE,

    /**
     * 商家修改密码
     */
     @ApiEnumProperty("商家修改密码")
    SUPPLIER_CHANGE_PASSWORD,

    /**
     * 平台修改密码
     */
     @ApiEnumProperty("平台修改密码")
    S2B_BOSS_CHANGE_PASSWORD,

    /**
     * 商家短信
     */
     @ApiEnumProperty("商家短信")
    SUPPLIER_SEND,

    /**
     * 平台短信
     */
     @ApiEnumProperty("平台短信")
    S2B_BOSS_SEND,

    /**
     * 供应商短信
     */
    @ApiEnumProperty("供应商短信")
    PROVIDER_SEND,

    /**
     * 供应商修改密码
     */
    @ApiEnumProperty("供应商修改密码")
    PROVIDER_CHANGE_PASSWORD;

    @JsonCreator
    public static VerifyType forValue(int ordinal) {
        return VerifyType.values()[ordinal];
    }

    @JsonValue
    public int toValue(){
        return this.ordinal();
    }
}
