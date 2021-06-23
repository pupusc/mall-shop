package com.wanmi.sbc.order.orderinvoice.request;

import lombok.Data;

import java.util.List;

/**
 * 订单开票参数
 * Created by CHENLI on 2017/5/8.
 */
@Data
public class OrderInvoiceEditRequest {
    private List<String> orderInvoiceIds;
}
