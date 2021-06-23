package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信支付退款接口参数
 */
@ApiModel
@Data
public class WxPayRefundRequest implements Serializable {

    private static final long serialVersionUID = 6918962309389680559L;

    /**
     * 必传参数
     */
    @ApiModelProperty(value = "公众账号ID")
    private String appid;                           //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;                          //商户号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;                       //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                            //签名

    @ApiModelProperty(value = "微信订单号以及商户订单号（二选一）")
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

    /**
     * 非必传参数
     */
    @ApiModelProperty(value = "签名类型")
    private String sign_type;                        //签名类型

    @ApiModelProperty(value = "退款货币种类")
    private String refund_fee_type;                  //退款货币种类

    @ApiModelProperty(value = "退款原因")
    private String refund_desc;                      //退款原因

    @ApiModelProperty(value = "退款资金来源")
    private String refund_account;                   //退款资金来源

}
