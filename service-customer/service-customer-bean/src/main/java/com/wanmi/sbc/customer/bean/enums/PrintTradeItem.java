package com.wanmi.sbc.customer.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum(dataType = "java.lang.String")
public enum PrintTradeItem {

    //商家名称
    @ApiEnumProperty("商家名称")
    STORE_NAME("STORE_NAME"),
    //客户名称
    @ApiEnumProperty("客户名称")
    CUSTOMER_NAME("CUSTOMER_NAME"),
    //订单号
    @ApiEnumProperty("订单号")
    TRADE_NUM("TRADE_NUM"),
    //订单时间
    @ApiEnumProperty("订单时间")
    TRADE_CREATE_TIME("TRADE_CREATE_TIME"),
    //收货人
    @ApiEnumProperty("收货人")
    DELIVERY_PERSON("DELIVERY_PERSON"),
    //收获地址
    @ApiEnumProperty("收获地址")
    DELIVERY_LOCATION("DELIVERY_LOCATION"),

    //商品清单
    @ApiEnumProperty("商品清单")
    GOODS_LIST("GOODS_LIST"),

    //应付总额
    @ApiEnumProperty("应付总额")
    TRADE_PRICE("TRADE_PRICE"),
    //商品金额
    @ApiEnumProperty("商品金额")
    GOODS_PRICE("GOODS_PRICE"),
    //满减优惠
    @ApiEnumProperty("满减优惠")
    MARKETING_REDUCTION("MARKETING_REDUCTION"),
    //满折优惠
    @ApiEnumProperty("满折优惠")
    MARKETING_DISCOUNT("MARKETING_DISCOUNT"),
    //订单改价
    @ApiEnumProperty("订单改价")
    SPECIAL_PRICE("SPECIAL_PRICE"),
    //配送费用
    @ApiEnumProperty("配送费用")
    DELIVERY_PRICE("DELIVERY_PRICE"),

    //订单备注
    @ApiEnumProperty("订单备注")
    TRADE_DESC("TRADE_DESC"),
    //客户签名
    @ApiEnumProperty("客户签名")
    CUSTOMER_SIGN("CUSTOMER_SIGN");

    PrintTradeItem(String desc) {
        this.desc = desc;
    }

    /**
     * 描述信息
     */
    private String desc;

    public String getDesc() {
        return desc;
    }

}