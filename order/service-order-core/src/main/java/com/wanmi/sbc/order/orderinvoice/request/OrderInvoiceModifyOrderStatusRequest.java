package com.wanmi.sbc.order.orderinvoice.request;

import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单开票参数
 * Created by weiwenhao on 2021/07/02.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceModifyOrderStatusRequest {



    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 订单支付状态
     */
    @ApiModelProperty(value = "订单支付状态")
    private PayOrderStatus payOrderStatus;


    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private FlowState orderStatus;






}
