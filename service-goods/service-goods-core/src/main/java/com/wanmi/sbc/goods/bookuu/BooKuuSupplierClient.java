package com.wanmi.sbc.goods.bookuu;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bookuu.request.GoodsCostPriceRequest;
import com.wanmi.sbc.goods.bookuu.request.GoodsStockRequest;
import com.wanmi.sbc.goods.bookuu.response.GoodsCostPriceResponse;
import com.wanmi.sbc.goods.bookuu.response.GoodsStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "sbc-service-dock")
public interface BooKuuSupplierClient {

    @RequestMapping(value = "/bk/goods/get-goods-stock",method = RequestMethod.POST)
    BaseResponse<List<GoodsStockResponse>> getGoodsStock(@RequestBody GoodsStockRequest goodsStockRequest);


    @RequestMapping(value = "/bk/goods/get-goods-cost-price",method = RequestMethod.POST)
    BaseResponse<List<GoodsCostPriceResponse>> getGoodsCostPrice(@RequestBody GoodsCostPriceRequest goodsCostPriceRequest);
}