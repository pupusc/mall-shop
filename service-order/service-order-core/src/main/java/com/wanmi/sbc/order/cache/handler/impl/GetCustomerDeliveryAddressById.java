package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p>收货地址</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:33
 */
public class GetCustomerDeliveryAddressById implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0),params[0]);
        return container;
    }
}
