package com.wanmi.sbc.order.cache.handler.impl;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.order.cache.handler.ICacheKeyHandler;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>缓存一系列营销价格</p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 13:53
 */
public class MarketingPluginGoodsListFilter implements ICacheKeyHandler<String, Object> {
    @Override
    public Map<String, Object> handle(Map<String, Object> container, Object... params) {
        if (params[0] instanceof List && params[1] instanceof CustomerVO){
            List<GoodsInfoVO> goodsInfoVOS = (List<GoodsInfoVO>) params[0];
            CustomerVO customerVO = (CustomerVO) params[1];
            if (CollectionUtils.isNotEmpty(goodsInfoVOS)){
                for (int i = 0; i < goodsInfoVOS.size(); i++) {
                    container.put(String.valueOf(i), goodsInfoVOS.get(i));
                }
            }
            container.put(String.valueOf(customerVO.getCustomerId()),customerVO.getCustomerId());
        }
        return container;
    }
}
