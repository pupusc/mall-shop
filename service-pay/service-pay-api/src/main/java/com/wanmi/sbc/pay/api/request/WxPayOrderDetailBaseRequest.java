package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WxPayOrderDetailBaseRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/9/17 14:03
 **/
@ApiModel
@Data
public class WxPayOrderDetailBaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /**
     * 非必传参数
     */
    @ApiModelProperty(value = "签名类型")
    private String sign_type;                        //签名类型
}
