package com.wanmi.sbc.goods.api.provider.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsInfoIdsRequest;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsIdResponse;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsInfoIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:08
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsAresQueryProvider")
public interface GoodsAresQueryProvider {

    /**
     * 根据spuId获取Sku
     * @param goodsAresListByGoodsIdRequest {@link GoodsAresListByGoodsIdRequest }
     * @return {@link GoodsAresListByGoodsIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/ares/list-by-goods-id")
    BaseResponse<GoodsAresListByGoodsIdResponse> listByGoodsId(@RequestBody @Valid GoodsAresListByGoodsIdRequest goodsAresListByGoodsIdRequest);

    /**
     * 根据spuIdList获取Sku
     * @param goodsAresListByGoodsIdsRequest {@link GoodsAresListByGoodsIdsRequest }
     * @return {@link GoodsAresListByGoodsIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/ares/list-by-goods-ids")
    BaseResponse<GoodsAresListByGoodsIdsResponse> listByGoodsIds(@RequestBody @Valid GoodsAresListByGoodsIdsRequest goodsAresListByGoodsIdsRequest);

    /**
     * 根据skuIdList获取Sku
     * @param goodsAresListByGoodsInfoIdsRequest {@link GoodsAresListByGoodsInfoIdsRequest }
     * @return {@link GoodsAresListByGoodsInfoIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/ares/list-by-goods-info-ids")
    BaseResponse<GoodsAresListByGoodsInfoIdsResponse> listByGoodsInfoIds(@RequestBody @Valid GoodsAresListByGoodsInfoIdsRequest goodsAresListByGoodsInfoIdsRequest);
}
