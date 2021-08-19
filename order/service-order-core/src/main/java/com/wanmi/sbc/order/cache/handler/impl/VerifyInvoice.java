package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;

import java.util.Map;

/**
 * <p>发票校验</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:30
 */
public class VerifyInvoice implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if(params[0] instanceof Invoice) {
            Invoice invoice = (Invoice) params[0];
            container.put(String.valueOf(0),invoice.getType());
        }
        if(params[1] instanceof Supplier) {
            Supplier supplier = (Supplier) params[1];
            container.put(String.valueOf(1),supplier.getSupplierId());
        }
        return container;
    }
}
