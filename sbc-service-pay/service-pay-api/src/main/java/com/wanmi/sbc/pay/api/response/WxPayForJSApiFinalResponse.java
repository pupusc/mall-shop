package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信公众号支付最终返回参数
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayForJSApiFinalResponse implements Serializable {

    @ApiModelProperty(value = "公众账号ID")
    private String appId;                   //公众账号ID

    @ApiModelProperty(value = "时间戳")
    private String timeStamp;               //时间戳

    @ApiModelProperty(value = "随机字符串")
    private String nonceStr;               //随机字符串

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
}
