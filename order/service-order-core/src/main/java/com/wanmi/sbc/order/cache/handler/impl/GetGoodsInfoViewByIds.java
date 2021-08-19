package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>商品</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:46
 */
public class GetGoodsInfoViewByIds implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if(params[0] instanceof List) {
            List skuIds = (List) params[0];
            for(int i=0;i<skuIds.size();i++) {
                container.put(String.valueOf(i),skuIds.get(i));
            }
        }
        return container;
    }
}
