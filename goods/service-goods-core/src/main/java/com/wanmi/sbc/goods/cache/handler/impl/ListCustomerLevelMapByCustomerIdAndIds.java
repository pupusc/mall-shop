package com.wanmi.sbc.goods.cache.handler.impl;

import com.wanmi.sbc.goods.cache.handler.ICacheKeyHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>店铺校验</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:31
 */
public class ListCustomerLevelMapByCustomerIdAndIds implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0), params[0]);
        return container;
    }
}
