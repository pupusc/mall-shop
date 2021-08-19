package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p>会员简要信息</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:51
 */
public class GetCustomerById implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0), params[0]);
        return container;
    }
}
