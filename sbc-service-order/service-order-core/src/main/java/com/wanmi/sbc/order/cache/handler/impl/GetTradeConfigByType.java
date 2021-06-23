package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;
import com.wanmi.sbc.setting.bean.enums.ConfigType;

import java.util.Map;

/**
 * <p>查询系统配置</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:50
 */
public class GetTradeConfigByType implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        ConfigType configType = (ConfigType) params[0];
        container.put(String.valueOf(0), configType.toValue());
        return container;
    }
}
