package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsStockProvider")
public interface EsGoodsStockProvider {

    @PostMapping("/elastic/${application.elastic.version}/goods/spu/sub/stock")
    BaseResponse subStockBySpuId(@RequestBody @Valid EsGoodsSpuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/sku/sub/stock")
    BaseResponse subStockBySkuId(@RequestBody @Valid EsGoodsSkuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/spu/reset/stock")
    BaseResponse resetStockBySpuId(@RequestBody @Valid EsGoodsSpuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/sku/reset/stock")
    BaseResponse resetStockBySkuId(@RequestBody @Valid EsGoodsSkuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/spu/batchReset/stock")
    BaseResponse batchResetStockBySpuId(@RequestBody @Valid EsGoodsSpuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/sku/batchReset/stock")
    BaseResponse batchResetStockBySkuId(@RequestBody @Valid EsGoodsSkuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/goodsinfo/batchReset/stock")
    BaseResponse batchResetGoodsInfoStockBySpuId(@RequestBody @Valid EsGoodsSpuStockSubRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/audit-status")
    BaseResponse updateWxAuditStatus(@RequestParam("goodsId") String goodsId);

}
