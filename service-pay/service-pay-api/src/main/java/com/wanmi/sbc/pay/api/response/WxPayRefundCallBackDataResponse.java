package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxPayRefundCallBackDataResponse implements Serializable {
    private static final long serialVersionUID = -8794814648528883062L;


    @ApiModelProperty(value = "微信订单号")
    private String transaction_id;      //微信订单号

    @ApiModelProperty(value = "商户退款单号")
    private String out_trade_no;        //商户退款单号

    @ApiModelProperty(value = "微信退款单号")
    private String refund_id;           //微信退款单号

    @ApiModelProperty(value = "商户退款单号")
    private String out_refund_no;       //商户退款单号

    @ApiModelProperty(value = "订单金额")
    private String total_fee;           //订单金额

    @ApiModelProperty(value = "应结订单金额")
    private String settlement_total_fee;    //应结订单金额

    @ApiModelProperty(value = "申请退款金额")
    private String refund_fee;          //申请退款金额

    @ApiModelProperty(value = "退款金额")
    private String settlement_refund_fee; //退款金额

    @ApiModelProperty(value = "退款状态")
    private String refund_status;       //退款状态

    @ApiModelProperty(value = "退款成功时间")
    private String success_time;        //退款成功时间

    @ApiModelProperty(value = "退款入账账户")
    private String refund_recv_accout;  //退款入账账户

    @ApiModelProperty(value = "退款资金来源")
    private String refund_account;      //退款资金来源

    @ApiModelProperty(value = "退款发起来源")
    private String refund_request_source;//退款发起来源

}
