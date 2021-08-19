package com.wanmi.sbc.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service-B
 * @description:
 * @create: 2020-07-16 15:51
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class OrderTodoResp implements Serializable {


    private static final long serialVersionUID = 8559099135805812224L;
    /**
     * 待付款
     */
    @ApiModelProperty(value = "待付款")
    private Long waitPay;

    /**
     * 待发货
     */
    @ApiModelProperty(value = "待发货")
    private Long waitDeliver;

    /**
     * 待收货
     */
    @ApiModelProperty(value = "待收货")
    private Long waitReceiving;

    /**
     * 待评价
     */
    @ApiModelProperty(value = "待评价")
    private Long waitEvaluate;

    /**
     * 退货、退款
     */
    @ApiModelProperty(value = "退货、退款")
    private Long refund;


}