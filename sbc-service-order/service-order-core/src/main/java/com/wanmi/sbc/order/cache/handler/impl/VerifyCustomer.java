package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p>会员校验</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:29
 */
public class VerifyCustomer implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0),params[0]);
        return container;
    }
}
