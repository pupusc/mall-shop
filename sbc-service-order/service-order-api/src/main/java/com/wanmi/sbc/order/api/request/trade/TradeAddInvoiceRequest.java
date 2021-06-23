package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.InvoiceDTO;
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
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAddInvoiceRequest implements Serializable {

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 发票
     */
    @ApiModelProperty(value = "发票")
    private InvoiceDTO invoiceDTO;

}
