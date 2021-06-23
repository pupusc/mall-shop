package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsDistributorGoodsListQueryRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoListRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoListResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.goods.service.EsGoodsInfoElasticService;
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
public class EsGoodsInfoElasticQueryController implements EsGoodsInfoElasticQueryProvider {

    @Autowired
    private EsGoodsInfoElasticService esGoodsInfoElasticService;

    @Override
    public BaseResponse<EsGoodsInfoResponse> page(@RequestBody @Valid EsGoodsInfoQueryRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.page(request));
    }

    @Override
    public BaseResponse<EsGoodsResponse> pageByGoods(@RequestBody @Valid EsGoodsInfoQueryRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.pageByGoods(request));
    }

    @Override
    public BaseResponse<EsGoodsInfoResponse> distributorGoodsListByCustomerId(@RequestBody @Valid EsGoodsInfoQueryRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.distributorGoodsListByCustomerId(request));
    }

    @Override
    public BaseResponse<EsGoodsInfoResponse> distributorGoodsList(@RequestBody @Valid EsDistributorGoodsListQueryRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.distributorGoodsList(request.getRequest(),
                request.getGoodsIdList()));
    }

    @Override
    public BaseResponse<EsGoodsInfoListResponse> listByIds(@RequestBody @Valid EsGoodsInfoListRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.listByIds(request));
    }
}
