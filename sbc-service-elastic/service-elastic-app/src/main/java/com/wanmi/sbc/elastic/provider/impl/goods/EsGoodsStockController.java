package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.elastic.goods.service.EsGoodsStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsGoodsStockController implements EsGoodsStockProvider {

    @Autowired
    private EsGoodsStockService esGoodsStockService;

    @Override
    public BaseResponse subStockBySpuId(@RequestBody @Valid EsGoodsSpuStockSubRequest request) {
        esGoodsStockService.subStockBySpuId(request.getSpuId(), request.getStock());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse subStockBySkuId(@RequestBody @Valid EsGoodsSkuStockSubRequest request) {
        esGoodsStockService.subStockBySkuId(request.getSkuId(), request.getStock());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse resetStockBySpuId(@Valid EsGoodsSpuStockSubRequest request) {
        esGoodsStockService.resetStockBySpuId(request.getSpuId(),request.getStock());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse resetStockBySkuId(@Valid EsGoodsSkuStockSubRequest request) {
        esGoodsStockService.resetStockBySkuId(request.getSkuId(),request.getStock());
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse batchResetStockBySpuId(@Valid EsGoodsSpuStockSubRequest request) {
        esGoodsStockService.batchResetStockBySpuId(request.getSpusMap());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchResetStockBySkuId(@Valid EsGoodsSkuStockSubRequest request) {
        esGoodsStockService.batchResetStockBySkuId(request.getSkusMap());
        return BaseResponse.SUCCESSFUL();
    }
}
