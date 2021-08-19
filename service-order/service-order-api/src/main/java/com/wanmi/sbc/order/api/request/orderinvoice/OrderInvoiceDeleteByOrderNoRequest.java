package com.wanmi.sbc.order.api.request.orderinvoice;

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
 * @Date: 2018-12-03 15:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceDeleteByOrderNoRequest implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

}
