package com.wanmi.sbc.order.api.response.orderinvoice;

import com.wanmi.sbc.account.bean.enums.InvoiceState;
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
 * @Date: 2018-12-03 15:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceCountByStateResponse implements Serializable {

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Long count;
}
