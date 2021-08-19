package com.wanmi.sbc.goods.api.provider.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsAndCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsCustomerPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsCustomerPriceBySkuIdsAndCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsCustomerPriceBySkuIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 10:08
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCustomerPriceQueryProvider")
public interface GoodsCustomerPriceQueryProvider {

    /**
     * 根据批量商品ID和客户ID查询商品会员价格
     * @param goodsCustomerPriceBySkuIdsAndCustomerIdRequest 包含：商品ID和会员ID {@link GoodsCustomerPriceBySkuIdsAndCustomerIdRequest }
     * @return {@link GoodsCustomerPriceBySkuIdsAndCustomerIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/price/customer/list-by-sku-ids-and-customer-id")
    BaseResponse<GoodsCustomerPriceBySkuIdsAndCustomerIdResponse> listBySkuIdsAndCustomerId(@RequestBody @Valid GoodsCustomerPriceBySkuIdsAndCustomerIdRequest goodsCustomerPriceBySkuIdsAndCustomerIdRequest);

    /**
     * 根据商品SkuID查询SKU客户价
     * @param goodsCustomerPriceBySkuIdsRequest 包含：商品Sku ID {@link GoodsCustomerPriceBySkuIdsRequest }
     * @return {@link GoodsCustomerPriceBySkuIdsResponse }
     */
    @PostMapping("/goods/${application.goods.version}/price/customer/list-by-sku-ids")
    BaseResponse<GoodsCustomerPriceBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsCustomerPriceBySkuIdsRequest goodsCustomerPriceBySkuIdsRequest);
}

