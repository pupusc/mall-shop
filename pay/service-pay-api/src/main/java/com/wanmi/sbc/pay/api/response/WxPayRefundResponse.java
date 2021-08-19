package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付退款返回参数
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayRefundResponse implements Serializable {
    private static final long serialVersionUID = -5021979520114517057L;

    @ApiModelProperty(value = "返回状态码")
    private String return_code;         //返回状态码

    @ApiModelProperty(value = "返回信息")
    private String return_msg;          //返回信息

    @ApiModelProperty(value = "业务结果")
    private String result_code;         //业务结果

    @ApiModelProperty(value = "错误代码")
    private String err_code;            //错误代码

    @ApiModelProperty(value = "错误代码描述")
    private String err_code_des;        //错误代码描述

    @ApiModelProperty(value = "公众账号ID")
    private String appid;               //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;              //商户号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;           //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                //签名

    @ApiModelProperty(value = "签名类型")
    private String sign_type;           //签名类型

    @ApiModelProperty(value = "微信订单号")
    private String transaction_id;      //微信订单号

    @ApiModelProperty(value = "商户退款单号")
    private String out_trade_no;        //商户退款单号

    @ApiModelProperty(value = "微信退款单号")
    private String refund_id;           //微信退款单号

    @ApiModelProperty(value = "退款金额")
    private String refund_fee;          //退款金额

    @ApiModelProperty(value = "应结退款金额")
    private String settlement_refund_fee; //应结退款金额

    @ApiModelProperty(value = "标价金额")
    private String total_fee;           //标价金额

    @ApiModelProperty(value = "应结订单金额")
    private String settlement_total_fee;    //应结订单金额

    @ApiModelProperty(value = "标价币种")
    private String fee_type;            //标价币种

    @ApiModelProperty(value = "现金支付金额")
    private String cash_fee;              //现金支付金额

    @ApiModelProperty(value = "现金支付币种")
    private String cash_fee_type;           //现金支付币种

    @ApiModelProperty(value = "现金退款金额")
    private String cash_refund_fee;                //现金退款金额

    @ApiModelProperty(value = "代金券类型")
    private String coupon_type_$n;           //代金券类型

    @ApiModelProperty(value = "代金券退款总金额")
    private String coupon_refund_fee;      //代金券退款总金额

    @ApiModelProperty(value = "单个代金券退款金额")
    private String coupon_refund_fee_$n	;        //单个代金券退款金额

    @ApiModelProperty(value = "退款代金券使用数量")
    private String coupon_refund_count;           //退款代金券使用数量

    @ApiModelProperty(value = "退款代金券ID")
    private String coupon_refund_id_$n	;          //退款代金券ID

}
