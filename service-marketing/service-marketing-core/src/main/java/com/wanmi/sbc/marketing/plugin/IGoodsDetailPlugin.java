package com.wanmi.sbc.marketing.plugin;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;

/**
 * 商品详情过滤器
 * Created by dyt on 2017/11/20.
 */
public interface IGoodsDetailPlugin {

    /**
     * 商品详情处理
     * @param detailResponse 商品详情数据
     * @param request 参数
     * @throws SbcRuntimeException
     */
    void goodsDetailFilter(GoodsInfoDetailByGoodsInfoResponse detailResponse, MarketingPluginRequest request);


}
