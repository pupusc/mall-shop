package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.List;
import java.util.Map;

public class ListCompanyStoreByCompanyIds implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if(params[0] instanceof List) {
            List companyIds = (List) params[0];
            for(int i=0;i<companyIds.size();i++) {
                container.put(String.valueOf(i),companyIds.get(i));
            }
        }
        return container;
    }
}
