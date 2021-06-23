package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TradePayOnlineCallBackRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/7/2 14:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradePayOnlineCallBackRequest {

    /**
     * 微信支付回调结果
     */
    @ApiModelProperty(value = "微信支付回调结果")
    private String wxPayCallBackResultStr;

    /**
     * 微信支付回调xml结果
     */
    @ApiModelProperty(value = "微信支付回调xml结果")
    private String wxPayCallBackResultXmlStr;

    /**
     * 支付宝支付回调结果
     */
    @ApiModelProperty(value = "支付宝支付回调结果")
    private String aliPayCallBackResultStr;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式0: 微信支付，1: 支付宝支付，2: 银联支付")
    private PayCallBackType payCallBackType;
}
