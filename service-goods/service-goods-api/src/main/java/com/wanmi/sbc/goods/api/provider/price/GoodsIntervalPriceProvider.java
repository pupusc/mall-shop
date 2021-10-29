package com.wanmi.sbc.goods.api.provider.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByGoodsAndSkuRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByGoodsAndSkuResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * com.wanmi.sbc.goods.api.provider.intervalprice.GoodsIntervalPriceProvider
 * 商品订货区间价格
 * @author lipeng
 * @dateTime 2018/11/6 下午2:23
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsIntervalPriceProvider")
public interface GoodsIntervalPriceProvider {

    /**
     * 为每个SKU填充区间价范围值intervalMinPrice,intervalMaxPrice,intervalPriceIds，并返回相应区间价结果
     *
     * @param request {@link GoodsIntervalPriceRequest}
     * @return 区间价结果 {@link GoodsIntervalPriceResponse}
     */
    @PostMapping("/goods/${application.goods.version}/price/interval/put")
    BaseResponse<GoodsIntervalPriceResponse> put(@RequestBody @Valid GoodsIntervalPriceRequest request);

    /**
     * 为每个SKU填充区间价范围值intervalMinPrice,intervalMaxPrice,intervalPriceIds，并返回相应区间价结果
     *
     * @param request {@link GoodsIntervalPriceByCustomerIdRequest}
     * @return 区间价结果 {@link GoodsIntervalPriceByCustomerIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/price/interval/put-by-customer-id")
    BaseResponse<GoodsIntervalPriceByCustomerIdResponse> putByCustomerId(
            @RequestBody @Valid GoodsIntervalPriceByCustomerIdRequest request);

    /**
     * 为每个SKU和SPU填充区间价范围值intervalMinPrice,intervalMaxPrice,intervalPriceIds，并返回相应区间价结果
     *
     * @param request {@link GoodsIntervalPriceByGoodsAndSkuRequest}
     * @return 区间价结果 {@link GoodsIntervalPriceByGoodsAndSkuResponse}
     */
    @PostMapping("/goods/${application.goods.version}/price/interval/put-goods-and-sku")
    BaseResponse<GoodsIntervalPriceByGoodsAndSkuResponse> putGoodsAndSku(
            @RequestBody @Valid GoodsIntervalPriceByGoodsAndSkuRequest request);
}
