package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信支付--统一下单请求参数--微信App支付
 */
@ApiModel
@Data
public class WxPayForAppRequest implements Serializable {

    private static final long serialVersionUID = 1351181861750433699L;

    /**---必填项---**/
    @ApiModelProperty(value = "公众账号ID")
    private String appid;               //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;              //商户号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;           //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                //签名

    @ApiModelProperty(value = "商品描述")
    private String body;                //商品描述

    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;        //商户订单号

    @ApiModelProperty(value = "总金额")
    private String total_fee;           //总金额

    @ApiModelProperty(value = "终端IP")
    private String spbill_create_ip;    //终端IP

    @ApiModelProperty(value = "通知地址")
    private String notify_url;          //通知地址

    @ApiModelProperty(value = "交易类型")
    private String trade_type;          //交易类型 微信浏览器内JSApi支付的交易类型为JSAPI

    /**---非必填项---**/
    @ApiModelProperty(value = "设备号")
    private String device_info;         //设备号

    @ApiModelProperty(value = "签名类型")
    private String sign_type;           //签名类型

    @ApiModelProperty(value = "商品详情")
    private String detail;              //商品详情

    @ApiModelProperty(value = "附加数据")
    private String attach;              //附加数据

    @ApiModelProperty(value = "货币类型")
    private String fee_type;            //货币类型

    @ApiModelProperty(value = "交易起始时间")
    private String time_start;          //交易起始时间

    @ApiModelProperty(value = "交易结束时间")
    private String time_expire;         //交易结束时间

    @ApiModelProperty(value = "商品标记")
    private String goods_tag;           //商品标记

    @ApiModelProperty(value = "商品ID")
    private String product_id;          //商品ID

    @ApiModelProperty(value = "指定支付方式")
    private String limit_pay;           //指定支付方式

    @ApiModelProperty(value = "用户标识")
    private String openid;              //用户标识

    @ApiModelProperty(value = "电子发票入口开放标识")
    private String receipt;             //电子发票入口开放标识

    @ApiModelProperty(value = "场景信息")
    private String scene_info;          //场景信息

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
