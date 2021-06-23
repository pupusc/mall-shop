package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信企业付款到零钱请求参数
 */
@ApiModel
@Data
public class WxPayCompanyPaymentInfoRequest implements Serializable {

    private static final long serialVersionUID = 4863792275850646830L;

    /**
     * 必传字段
     */
    @ApiModelProperty(value = "商户订单号")
    private String partner_trade_no;    //商户订单号

    @ApiModelProperty(value = "用户openid")
    private String openid;              //用户openid,商户appid下，某用户的openid

    @ApiModelProperty(value = "校验用户姓名")
    private String check_name;          //校验用户姓名;选项NO_CHECK：不校验真实姓名；FORCE_CHECK：强校验真实姓名

    @ApiModelProperty(value = "收款用户姓名")
    private String re_user_name;        //收款用户姓名;收款用户真实姓名。如果check_name设置为FORCE_CHECK，则必填用户真实姓名

    @ApiModelProperty(value = "企业付款金额")
    private String amount;              //金额,企业付款金额，单位为分

    @ApiModelProperty(value = "企业付款备注")
    private String desc;                //企业付款备注,企业付款备注，必填。注意：备注中的敏感词会被转成字符*

    @ApiModelProperty(value = "终端IP")
    private String spbill_create_ip;    //终端IP

    @ApiModelProperty(value = "付款类型")
    private WxPayTradeType payType;

    /**
     * 非必传字段
     */
    @ApiModelProperty(value = "设备号")
    private String device_info;         //设备号


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 小程序appid
     * 小程序配置是在setting模块。pay模块无法获取。被迫无奈，从bff传下去
     */
    @ApiModelProperty(value = "小程序appid")
    private String miniAppId;

}
