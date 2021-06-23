package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信支付企业支付到零钱接口参数
 */
@ApiModel
@Data
public class WxPayCompanyPaymentRequest implements Serializable {

    private static final long serialVersionUID = -4196600838496718707L;

    /**
     * 必传参数
     */
    @ApiModelProperty(value = "商户账号appid")
    private String mch_appid;           //商户账号appid：申请商户号的appid或商户号绑定的appid

    @ApiModelProperty(value = "商户号")
    private String mchid;               //商户号

    @ApiModelProperty(value = "随机字符串")
    private String nonce_str;           //随机字符串

    @ApiModelProperty(value = "签名")
    private String sign;                //签名

    @ApiModelProperty(value = "商户订单号")
    private String partner_trade_no;    //商户订单号

    @ApiModelProperty(value = "用户openid")
    private String openid;              //用户openid,商户appid下，某用户的openid

    @ApiModelProperty(value = "校验用户姓名")
    private String check_name;          //校验用户姓名;选项NO_CHECK：不校验真实姓名FORCE_CHECK：强校验真实姓名

    @ApiModelProperty(value = "企业付款金额")
    private String amount;              //金额,企业付款金额，单位为分

    @ApiModelProperty(value = "企业付款备注")
    private String desc;                //企业付款备注,企业付款备注，必填。注意：备注中的敏感词会被转成字符*

    @ApiModelProperty(value = "终端IP")
    private String spbill_create_ip;    //终端IP

    /**---非必填项---**/
    @ApiModelProperty(value = "设备号")
    private String device_info;         //设备号

    @ApiModelProperty(value = "收款用户姓名")
    private String re_user_name;        //收款用户姓名;收款用户真实姓名。如果check_name设置为FORCE_CHECK，则必填用户真实姓名

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
