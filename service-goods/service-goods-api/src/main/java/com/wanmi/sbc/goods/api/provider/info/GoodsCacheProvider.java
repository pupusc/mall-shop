package com.wanmi.sbc.goods.api.provider.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商品info操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCacheProvider")
public interface GoodsCacheProvider {

    /**
     * 查询商品列表缓存
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/list-goods-cache-by-ids")
    BaseResponse<GoodsCachesByIdsResponse> listGoodsByIds(@RequestBody @Valid GoodsCachesByIdsRequest request);

    /**
     * 查询单品列表缓存
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/list-goods-info-cache-by-ids")
    BaseResponse<GoodsInfoCachesByIdsResponse> listGoodsInfosByIds(@RequestBody @Valid GoodsInfoCachesByIdsRequest request);
}
