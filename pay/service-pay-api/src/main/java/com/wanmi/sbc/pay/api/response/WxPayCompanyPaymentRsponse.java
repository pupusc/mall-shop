package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付企业支付到零钱接口返回参数
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayCompanyPaymentRsponse implements Serializable {

    private static final long serialVersionUID = 6874251518818128801L;

    @ApiModelProperty(value = "返回状态码")
    private String return_code;                      //返回状态码：SUCCESS/FAIL；此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断

    @ApiModelProperty(value = "返回信息")
    private String return_msg;                       //返回信息：返回信息，如非空，为错误原因 签名失败 参数格式校验错误

    @ApiModelProperty(value = "商户appid")
    private String mch_appid;                        //商户appid

    @ApiModelProperty(value = "商户号")
    private String mchid;                           //商户号

    @ApiModelProperty(value = "设备号")
    private String device_info;                      //设备号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;                        //随机字符串

    @ApiModelProperty(value = "业务结果")
    private String result_code;                      //业务结果

    @ApiModelProperty(value = "错误代码")
    private String err_code;                         //错误代码

    @ApiModelProperty(value = "错误代码描述")
    private String err_code_des;                     //错误代码描述

    @ApiModelProperty(value = "商户订单号")
    private String partner_trade_no;                 //商户订单号

    @ApiModelProperty(value = "微信付款单号")
    private String payment_no;                       //微信付款单号

    @ApiModelProperty(value = "付款成功时间")
    private String payment_time;                     //付款成功时间，企业付款成功时间

}
