package com.wanmi.sbc.account.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>支付方式</p>
 * Created by of628-wenzhi on 2017-12-05-下午4:18.
 */
@ApiEnum(dataType = "java.lang.String")
public enum PayWay {
    /**
     * 银联
     */
    @ApiEnumProperty("银联")
    UNIONPAY("银联"),

    /**
     * 微信
     */
    @ApiEnumProperty("微信")
    WECHAT("微信"),

    /**
     * 支付宝
     */
    @ApiEnumProperty("支付宝")
    ALIPAY("支付宝"),

    /**
     * 预存款
     */
    @ApiEnumProperty("预存款")
    ADVANCE("预存款"),

    /**
     * 积分
     */
    @ApiEnumProperty("积分兑换")
    POINT("积分兑换"),

    /**
     * 转账汇款
     */
    @ApiEnumProperty("转账汇款")
    CASH("转账汇款"),
    /**
     * 企业银联
     */
    @ApiEnumProperty("企业银联")
    UNIONPAY_B2B("企业银联"),

    /**
     * 优惠券
     */
    @ApiEnumProperty("优惠券")
    COUPON("优惠券"),

    /**
     * 余额
     */
    @ApiEnumProperty("余额")
    BALANCE("余额"),

    /**
     * 其他
     */
    @ApiEnumProperty("其他")
    OTHER("其他");



    private String desc;

    PayWay(String desc) {
        this.desc = desc;
    }

    @JsonCreator
    public PayWay fromValue(String name) {
        return PayWay.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

    public String getDesc() {
        return desc;
    }

}
