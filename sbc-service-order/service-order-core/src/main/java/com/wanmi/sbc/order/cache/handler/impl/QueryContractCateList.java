package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p>查询签约分类</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:48
 */
public class QueryContractCateList implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0), params[0]);
        container.put(String.valueOf(1), params[1]);
        return container;
    }
}
