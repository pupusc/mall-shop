package com.wanmi.sbc.marketing.cache.handler.impl;

import com.wanmi.sbc.marketing.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * 查询分销员信息
 */
public class GetDistributorByCustomerId implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf(0), params[0]);
        return container;
    }
}
