package com.wanmi.sbc.marketing.plugin;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.request.MarketingPluginRequest;

import java.util.List;

/**
 * 商品列表过滤器
 * Created by dyt on 2017/11/20.
 */
public interface IGoodsListPlugin {

    /**
     * 商品列表处理
     * @param goodsInfos 商品数据
     * @param request 参数
     * @throws SbcRuntimeException
     */
    void goodsListFilter(List<GoodsInfoVO> goodsInfos, MarketingPluginRequest request);

}
