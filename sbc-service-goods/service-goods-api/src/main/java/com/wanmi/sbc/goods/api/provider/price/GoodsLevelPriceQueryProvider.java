package com.wanmi.sbc.goods.api.provider.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsAndLevelIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsLevelPriceBySkuIdsAndLevelIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsLevelPriceBySkuIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 10:08
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsLevelPriceQueryProvider")
public interface GoodsLevelPriceQueryProvider {

    /**
     * 根据批量商品ID和批量等级查询SKU的级别价
     * @param goodsLevelPriceBySkuIdsAndLevelIdsRequest 包含：商品ID和会员等级ID {@link GoodsLevelPriceBySkuIdsAndLevelIdsRequest }
     * @return {@link GoodsLevelPriceBySkuIdsAndLevelIdsResponse }
    */
    @PostMapping("/goods/${application.goods.version}/price/level/list-by-sku-ids-and-level-ids")
    BaseResponse<GoodsLevelPriceBySkuIdsAndLevelIdsResponse> listBySkuIdsAndLevelIds(@RequestBody @Valid GoodsLevelPriceBySkuIdsAndLevelIdsRequest goodsLevelPriceBySkuIdsAndLevelIdsRequest);

    /**
     * 根据商品SkuID查询SKU的级别价
     * @param goodsLevelPriceBySkuIdsRequest 包含：skuID {@link GoodsLevelPriceBySkuIdsRequest }
     * @return {@link GoodsLevelPriceBySkuIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/price/level/list-by-sku-ids")
    BaseResponse<GoodsLevelPriceBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest);
}
