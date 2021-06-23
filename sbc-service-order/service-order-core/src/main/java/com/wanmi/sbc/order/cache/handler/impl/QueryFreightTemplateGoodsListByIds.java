package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>查询运费模板</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:49
 */
public class QueryFreightTemplateGoodsListByIds implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if(params[0] instanceof List) {
            List tempIdList = (List) params[0];
            for(int i=0;i<tempIdList.size();i++) {
                container.put(String.valueOf(i),tempIdList.get(i));
            }
        }
        return container;
    }
}
