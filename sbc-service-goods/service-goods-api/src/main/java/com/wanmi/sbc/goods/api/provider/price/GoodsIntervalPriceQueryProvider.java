package com.wanmi.sbc.goods.api.provider.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceListBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceListBySkuIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 商品区间价查询服务
 * @author daiyitian
 * @dateTime 2018/11/13 14:57
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsIntervalPriceQueryProvider")
public interface GoodsIntervalPriceQueryProvider {

    /**
     * 根据skuIds批量查询商品区间价列表
     *
     * @param request 包含skuIds的查询请求结构 {@link GoodsIntervalPriceListBySkuIdsRequest }
     * @return 商品区间价列表 {@link GoodsIntervalPriceListBySkuIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/price/interval/list-by-goods-ids")
    BaseResponse<GoodsIntervalPriceListBySkuIdsResponse> listByGoodsIds(@RequestBody @Valid
                                                                                GoodsIntervalPriceListBySkuIdsRequest
                                                                                request);
}
