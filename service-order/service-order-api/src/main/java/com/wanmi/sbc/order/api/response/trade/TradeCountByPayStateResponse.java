package com.wanmi.sbc.order.api.response.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-06 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeCountByPayStateResponse implements Serializable {

    /**
     * 待审核
     */
    @ApiModelProperty(value = "待审核")
    private Long waitAudit;

    /**
     * 待付款
     */
    @ApiModelProperty(value = "待付款")
    private Long waitPay = 0L;

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
     * 待审核订单 true:开启 false:关闭
     */
    @ApiModelProperty(value = "待审核订单", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean tradeCheckFlag = false;

}
