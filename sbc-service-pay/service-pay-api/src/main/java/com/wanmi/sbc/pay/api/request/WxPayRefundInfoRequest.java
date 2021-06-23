package com.wanmi.sbc.pay.api.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信支付退款请求类--用于其他服务调用，不能直接作为调用微信接口参数使用
 */
@ApiModel
@Data
public class WxPayRefundInfoRequest implements Serializable {
    private static final long serialVersionUID = -8340271949337463921L;

    @ApiModelProperty(value = "微信订单号以及商户订单号")
    private String transaction_id;                  //微信订单号以及商户订单号（二选一）

    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;                    //商户订单号

    @ApiModelProperty(value = "商户退款单号")
    private String out_refund_no;                   //商户退款单号

    @ApiModelProperty(value = "订单金额")
    private String total_fee;                       //订单金额

    @ApiModelProperty(value = "退款金额")
    private String refund_fee;                      //退款金额

    @ApiModelProperty(value = "退款结果通知url")
    private String notify_url;                      //退款结果通知url

    @ApiModelProperty(value = "签名类型")
    private String sign_type;                       //签名类型

    @ApiModelProperty(value = "退款货币种类")
    private String refund_fee_type;                 //退款货币种类

    @ApiModelProperty(value = "退款原因")
    private String refund_desc;                     //退款原因

    @ApiModelProperty(value = "退款资金来源")
    private String refund_account;                  //退款资金来源

    @ApiModelProperty(value = "对应退款的支付方式")
    private String pay_type;                        //对应退款的支付方式--APP:APP支付微信支付类型--为app，对应调用参数对应开放平台参数； "PC/H5/JSAPI";微信支付类型--为PC/H5/JSAPI，对应调用参数对应公众平台参数

    @ApiModelProperty(value = "退款类型")
    private String refund_type;                     //退款类型：REPEATPAY：重复支付


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
