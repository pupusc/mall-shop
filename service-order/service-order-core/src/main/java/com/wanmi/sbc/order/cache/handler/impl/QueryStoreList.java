package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>店铺查询</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:33
 */
public class QueryStoreList implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if(params[0] instanceof List) {
            List storeIds = (List) params[0];
            for(int i=0;i<storeIds.size();i++) {
                container.put(String.valueOf(i),storeIds.get(i));
            }
        }
        return container;
    }
}
