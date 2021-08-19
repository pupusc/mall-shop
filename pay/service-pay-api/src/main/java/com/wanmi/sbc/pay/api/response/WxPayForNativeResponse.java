package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付统一下单返回参数--扫码支付
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayForNativeResponse implements Serializable {

    private static final long serialVersionUID = 3175547149334572430L;

    @ApiModelProperty(value = "返回状态码")
    private String return_code;             //返回状态码

    @ApiModelProperty(value = "返回信息")
    private String return_msg;              //返回信息

    @ApiModelProperty(value = "公众账号ID")
    private String appid;                   //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;                  //商户号

    @ApiModelProperty(value = "设备号")
    private String device_info;             //设备号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;               //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                    //签名

    @ApiModelProperty(value = "业务结果")
    private String result_code;             //业务结果

    @ApiModelProperty(value = "错误代码")
    private String err_code;                //错误代码

    @ApiModelProperty(value = "错误代码描述")
    private String err_code_des;            //错误代码描述

    @ApiModelProperty(value = "交易类型")
    private String trade_type;              //交易类型

    @ApiModelProperty(value = "预支付交易会话标识")
    private String prepay_id;               //预支付交易会话标识

    @ApiModelProperty(value = "二维码链接")
    private String code_url;                //二维码链接

    @ApiModelProperty(value = "当前系统时间戳")
    private String time_stamp;              //当前系统时间戳
}
