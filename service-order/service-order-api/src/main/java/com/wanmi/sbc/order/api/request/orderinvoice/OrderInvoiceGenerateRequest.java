package com.wanmi.sbc.order.api.request.orderinvoice;

import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.order.bean.dto.OrderInvoiceDTO;
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
 * @Date: 2018-12-03 10:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class OrderInvoiceGenerateRequest implements Serializable {

    /**
     * 开票类
     */
    @ApiModelProperty(value = "开票类")
    private OrderInvoiceDTO orderInvoiceDTO;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private String employeeId;

    /**
     * 发票状态
     */
    @ApiModelProperty(value = "发票状态")
    private InvoiceState invoiceState;

}
