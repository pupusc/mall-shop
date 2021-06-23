package com.wanmi.sbc.goods.api.provider.presellsale;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleGoodsByGoodsInfoRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleGoodsResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.goods.name}", contextId = "PresellSaleGoodsQueryProvider")
public interface PresellSaleGoodsQueryProvider {

    /**
     * 根据预售活动商品关联id查询预售活动关联商品信息
     *
     * @param request 包含预售活动id查询请求结构 {@link PresellSaleGoodsByIdRequest}
     * @return 预售活动关联商品信息 {@link PresellSaleGoodsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell_sale_good_id")
    BaseResponse<PresellSaleGoodsResponse> presellSaleGoodsById(@RequestBody @Valid PresellSaleGoodsByIdRequest request);


    /**
     * 根据预售活动商品关联id查询预售活动关联商品信息
     *
     * @param request 包含预售活动id和预售活动关联商品id查询请求结构 {@link PresellSaleGoodsByGoodsInfoRequest}
     * @return 预售活动关联商品信息 {@link PresellSaleGoodsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell_sale_good_sku_id")
    BaseResponse<PresellSaleGoodsResponse> presellSaleGoodsByGoodsInfoId(@RequestBody PresellSaleGoodsByGoodsInfoRequest request);
}
