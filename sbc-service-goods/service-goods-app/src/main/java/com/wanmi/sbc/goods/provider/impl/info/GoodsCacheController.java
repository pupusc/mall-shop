package com.wanmi.sbc.goods.provider.impl.info;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.info.GoodsCacheProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsCachesByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoCachesByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsCachesByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoCachesByIdsResponse;
import com.wanmi.sbc.goods.info.service.GoodsCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 商品缓存controller
 */
@RestController
@Validated
public class GoodsCacheController implements GoodsCacheProvider {

    @Autowired
    private GoodsCacheService goodsCacheService;

    /**
     * 查询商品列表缓存
     * @param request
     * @return
     */
    @Override
    public BaseResponse<GoodsCachesByIdsResponse> listGoodsByIds(@RequestBody @Valid GoodsCachesByIdsRequest request) {
        return BaseResponse.success(new GoodsCachesByIdsResponse(goodsCacheService.listGoodsByIds(request.getGoodsIds())));
    }

    /**
     * 查询单品列表缓存
     * @param request
     * @return
     */
    @Override
    public BaseResponse<GoodsInfoCachesByIdsResponse> listGoodsInfosByIds(@RequestBody @Valid GoodsInfoCachesByIdsRequest request) {
        return BaseResponse.success(new GoodsInfoCachesByIdsResponse(goodsCacheService.listGoodsInfosByIds(request.getGoodsInfoIds(), request.getType())));
    }
}
