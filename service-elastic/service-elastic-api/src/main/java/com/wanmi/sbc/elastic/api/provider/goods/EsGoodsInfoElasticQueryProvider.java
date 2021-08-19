package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsDistributorGoodsListQueryRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoListResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsInfoElasticQueryProvider")
public interface EsGoodsInfoElasticQueryProvider {

    @PostMapping("/elastic/${application.elastic.version}/goods/page")
    BaseResponse<EsGoodsInfoResponse> page(@RequestBody @Valid EsGoodsInfoQueryRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/page/by/goods")
    BaseResponse<EsGoodsResponse> pageByGoods(@RequestBody @Valid EsGoodsInfoQueryRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/distributor/list/by/customer/id")
    BaseResponse<EsGoodsInfoResponse> distributorGoodsListByCustomerId(@RequestBody @Valid EsGoodsInfoQueryRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/distributor/list")
    BaseResponse<EsGoodsInfoResponse> distributorGoodsList(@RequestBody @Valid EsDistributorGoodsListQueryRequest request);

    /**
     * 根据skuId列表查询sku信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/goods/list-by-ids")
    BaseResponse<EsGoodsInfoListResponse> listByIds(@RequestBody @Valid EsGoodsInfoListRequest request);
}
