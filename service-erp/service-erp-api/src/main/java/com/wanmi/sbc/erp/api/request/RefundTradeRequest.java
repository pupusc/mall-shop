package com.wanmi.sbc.erp.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 退款中止ERP发货
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-07 16:25
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundTradeRequest implements Serializable {

    /**
     * 主订单号
     */
    @ApiModelProperty(value = "主订单号")
    private String tid;

    /**
     * 子订单号(商品对应的oid)
     */
    @ApiModelProperty(value = "子订单号")
    private String oid;

    /**
     * 商城退款状态,erp系统定义用户发起退款申请操作之后，状态就是"退款中"
     */
    @ApiModelProperty(value = "商城退款状态")
    private int refundState;

    private Long providerId;
}
