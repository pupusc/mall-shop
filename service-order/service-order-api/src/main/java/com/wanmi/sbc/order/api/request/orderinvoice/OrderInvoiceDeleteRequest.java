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
 * @Date: 2018-12-03 14:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceDeleteRequest implements Serializable {

    /**
     * 开票id
     */
    @ApiModelProperty(value = "开票id")
    private String orderInvoiceId;

}
