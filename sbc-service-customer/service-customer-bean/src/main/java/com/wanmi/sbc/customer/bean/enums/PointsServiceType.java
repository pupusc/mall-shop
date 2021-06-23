package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 会员积分业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信
 * 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除 18会员导入新增积分
 */
@ApiEnum
public enum PointsServiceType {
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
    ORDERCOMPLETION,
    @ApiEnumProperty("订单抵扣")
    ORDER_DEDUCTION,
    @ApiEnumProperty("优惠券兑换")
    COUPON_EXCHANGE,
    @ApiEnumProperty("积分兑换")
    POINTS_EXCHANGE,
    @ApiEnumProperty("退单返还")
    RETURN_ORDER_BACK,
    @ApiEnumProperty("订单取消返还")
    ORDER_CANCEL_BACK,
    @ApiEnumProperty("积分过期")
    EXPIRE,
    @ApiEnumProperty("会员导入增加积分")
    CUSTOMER_IMPORT,

    @ApiEnumProperty("权益发放")
    RIGHTS;


    @JsonCreator
    public PointsServiceType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}