package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>根据商品ID->店铺分类</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:53
 */
public class ListStoreCateByGoodsIds implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        List<String> spuIds = (List<String>) params[0];
        if (CollectionUtils.isNotEmpty(spuIds)){
            for (int i = 0; i < spuIds.size(); i++) {
                container.put(String.valueOf(i), spuIds.get(i));
            }
        }
        return container;
    }
}
