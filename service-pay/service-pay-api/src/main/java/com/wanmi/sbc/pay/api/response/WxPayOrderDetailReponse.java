package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName WxPayOrderDetailReponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/9/17 14:19
 **/
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayOrderDetailReponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "返回状态码")
    private String return_code;                      //返回状态码

    @ApiModelProperty(value = "返回信息")
    private String return_msg;          //返回信息

    /**
     * 交易状态:
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（付款码支付）
     * USERPAYING--用户支付中（付款码支付）
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @ApiModelProperty(value = "交易状态")
    private String trade_state;

    @ApiModelProperty(value = "公众账号ID")
    private String appid;                            //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;                           //商户号

    @ApiModelProperty(value = "设备号")
    private String device_info;                      //设备号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;                        //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                             //签名

    @ApiModelProperty(value = "签名类型")
    private String sign_type;                        //签名类型

    @ApiModelProperty(value = "业务结果")
    private String result_code;                      //业务结果

    @ApiModelProperty(value = "错误代码")
    private String err_code;                         //错误代码

    @ApiModelProperty(value = "错误代码描述")
    private String err_code_des;                     //错误代码描述

    @ApiModelProperty(value = "用户标识")
    private String openid;                           //用户标识

    @ApiModelProperty(value = "是否关注公众账号用户是否关注公众账号")
    private String is_subscribe;                     //是否关注公众账号用户是否关注公众账号，Y-关注，N-未关注

    @ApiModelProperty(value = "交易类型")
    private String trade_type;                       //交易类型

    @ApiModelProperty(value = "付款银行")
    private String bank_type;                        //付款银行

    @ApiModelProperty(value = "订单金额")
    private String total_fee;                        //订单金额

    @ApiModelProperty(value = "应结订单金额")
    private String settlement_total_fee;             //应结订单金额

    @ApiModelProperty(value = "货币种类")
    private String fee_type;                         //货币种类

    @ApiModelProperty(value = "现金支付金额")
    private String cash_fee;                         //现金支付金额

    @ApiModelProperty(value = "现金支付货币类型")
    private String cash_fee_type;                    //现金支付货币类型

    @ApiModelProperty(value = "总代金券金额")
    private String coupon_fee;                       //总代金券金额

    @ApiModelProperty(value = "代金券使用数量")
    private String coupon_count;                     //代金券使用数量

    @ApiModelProperty(value = "代金券类型")
    private String coupon_type_$n;                   //代金券类型

    @ApiModelProperty(value = "代金券ID")
    private String coupon_id_$n	;                    //代金券ID

    @ApiModelProperty(value = "单个代金券支付金额")
    private String coupon_fee_$n;                    //单个代金券支付金额

    @ApiModelProperty(value = "微信支付订单号")
    private String transaction_id;                   //微信支付订单号

    @ApiModelProperty(value = "商户订单号")
    private String out_trade_no;                     //商户订单号

    @ApiModelProperty(value = "商家数据包")
    private String attach;                           //商家数据包

    @ApiModelProperty(value = "支付完成时间支付完成时间")
    private String time_end;                         //支付完成时间支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。

    @ApiModelProperty(value = "交易状态描述")
    private String trade_state_desc; //交易状态描述
}
