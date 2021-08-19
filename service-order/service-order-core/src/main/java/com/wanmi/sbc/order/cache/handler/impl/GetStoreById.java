package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;

import java.util.Map;

/**
 * <p></p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 10:08
 */
public class GetStoreById implements ICacheKeyHandler<String, Object> {

    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        container.put(String.valueOf( params[0]), params[0]);
        return container;
    }
}
