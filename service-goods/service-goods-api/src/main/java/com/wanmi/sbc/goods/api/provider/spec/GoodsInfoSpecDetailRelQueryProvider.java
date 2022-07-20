package com.wanmi.sbc.goods.api.provider.spec;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySpuIdsRequest;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySkuIdsResponse;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySpuIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 14:57
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsInfoSpecDetailRelQueryProvider")
public interface GoodsInfoSpecDetailRelQueryProvider {

    /**
     * 根据spuid 和skuid查询
     * @param goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest 包含：spu ID和sku ID {@link GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest }
     * @return {@link GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/info/spec/detail/rel/list-by-goods-id-and-sku-id")
    BaseResponse<GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse> listByGoodsIdAndSkuId(@RequestBody @Valid GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest);

    /**
     * 根据多个SkuID查询
     * @param goodsInfoSpecDetailRelBySkuIdsRequest 包含：多个sku ID {@link GoodsInfoSpecDetailRelBySkuIdsRequest }
     * @return {@link GoodsInfoSpecDetailRelBySkuIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/info/spec/detail/rel/list-by-sku-ids")
    BaseResponse<GoodsInfoSpecDetailRelBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsInfoSpecDetailRelBySkuIdsRequest goodsInfoSpecDetailRelBySkuIdsRequest);

    /**
     * 根据多个SpuID查询
     */
    @PostMapping("/goods/${application.goods.version}/info/spec/detail/rel/list-by-spu-ids")
    BaseResponse<GoodsInfoSpecDetailRelBySpuIdsResponse> listBySpuIds(@RequestBody @Valid GoodsInfoSpecDetailRelBySpuIdsRequest goodsInfoSpecDetailRelBySkuIdsRequest);
}
