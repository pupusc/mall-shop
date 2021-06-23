package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信支付退款异步回调返回数据参数
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayRefundCallBackResponse implements Serializable {
    private static final long serialVersionUID = 2138501729302366774L;

    @ApiModelProperty(value = "返回状态码")
    private String return_code;         //返回状态码

    @ApiModelProperty(value = "返回信息")
    private String return_msg;          //返回信息

    @ApiModelProperty(value = "公众账号ID")
    private String appid;               //公众账号ID

    @ApiModelProperty(value = "商户号")
    private String mch_id;              //商户号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;           //随机字符串

    @ApiModelProperty(value = "加密信息")
    private String req_info;            //加密信息

}
