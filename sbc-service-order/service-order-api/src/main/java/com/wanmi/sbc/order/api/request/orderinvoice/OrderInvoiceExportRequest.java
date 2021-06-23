package com.wanmi.sbc.order.api.request.orderinvoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 14:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class OrderInvoiceExportRequest implements Serializable {

    /**
     * 导出传输列表
     */
    @ApiModelProperty(value = "导出传输列表")
    private List<OrderInvoiceExportRequest> orderInvoiceExportRequestList;

    /**
     * 输出流
     */
    @ApiModelProperty(value = "输出流")
    private OutputStream outputStream;

}
