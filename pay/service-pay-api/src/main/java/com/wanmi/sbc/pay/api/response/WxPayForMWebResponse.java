package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付统一下单返回参数--h5支付
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayForMWebResponse implements Serializable {

    private static final long serialVersionUID = 784024521255748772L;

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

    @ApiModelProperty(value = "mweb_url为拉起微信支付收银台的中间页面")
    private String mweb_url;                //mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟。
    @ApiModelProperty(value = "付费会员购买记录流水号")
    private String payCode;
}
