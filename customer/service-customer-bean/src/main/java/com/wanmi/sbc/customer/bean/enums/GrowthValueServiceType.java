package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 成长值获取业务类型、0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址 10关注店铺 11订单完成
 *
 * @author yang
 * @since 2019/2/22
 */
@ApiEnum
public enum GrowthValueServiceType {
    @ApiEnumProperty("签到")
    SIGNIN,
    @ApiEnumProperty("注册")
    REGISTER,
    @ApiEnumProperty("分享商品")
    SHARE,
    @ApiEnumProperty("分享注册")
    SHAREREGISTER,
    @ApiEnumProperty("分享购买")
    SHAREPURCHASE,
    @ApiEnumProperty("评论商品")
    EVALUATE,
    @ApiEnumProperty("晒单")
    SHAREORDER,
    @ApiEnumProperty("上传头像/完善个人信息")
    PERFECTINFO,
    @ApiEnumProperty("绑定微信")
    BINDINGWECHAT,
    @ApiEnumProperty("添加收货地址")
    ADDSHIPPINGADDRESS,
    @ApiEnumProperty("关注店铺")
    FOCUSONSTORE,
    @ApiEnumProperty("订单完成")
    ORDERCOMPLETION;


    @JsonCreator
    public GrowthValueServiceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}