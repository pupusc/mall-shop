package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Builder
@Data
public class AutoUpdateInvoiceRequest implements Serializable {
    /**
     * 对应的trade id
     */
    private String tradeId;

//    private InvoiceType invoiceType;
//
//    private InvoiceState invoiceState;
}
