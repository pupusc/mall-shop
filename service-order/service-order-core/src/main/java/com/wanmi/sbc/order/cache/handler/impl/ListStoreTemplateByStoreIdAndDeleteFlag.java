package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p>店铺物流价格模板</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:55
 */
public class ListStoreTemplateByStoreIdAndDeleteFlag implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf( params[0]), params[0]);
        if (params[1] instanceof DeleteFlag){
            DeleteFlag param = (DeleteFlag) params[1];
            container.put(String.valueOf( param.toValue()), param.toValue());
        }
        return container;
    }
}
