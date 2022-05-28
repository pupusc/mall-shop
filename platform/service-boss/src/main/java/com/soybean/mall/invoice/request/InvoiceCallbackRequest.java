package com.soybean.mall.invoice.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 回调状态
 */
@Data
public class InvoiceCallbackRequest {
    /**
     * 回调的订单id列表
     */
    @NotEmpty
    private List<String> orderCodes;
}
