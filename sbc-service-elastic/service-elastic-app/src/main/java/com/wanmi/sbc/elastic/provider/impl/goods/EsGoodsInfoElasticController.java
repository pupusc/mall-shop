package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.*;
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
public class EsGoodsInfoElasticController implements EsGoodsInfoElasticProvider {

    @Autowired
    private EsGoodsInfoElasticService esGoodsInfoElasticService;

    @Override
    public BaseResponse delBrandIds(@RequestBody @Valid EsBrandDeleteByIdsRequest request) {
        esGoodsInfoElasticService.delBrandIds(request.getDeleteIds(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initEsGoodsInfo(@RequestBody @Valid EsGoodsInfoRequest request) {
        esGoodsInfoElasticService.initEsGoodsInfo(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<Boolean> modifyDistributionGoodsAudit(@RequestBody @Valid EsGoodsInfoModifyDistributionGoodsAuditRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.modifyDistributionGoodsAudit(request));
    }

    @Override
    public BaseResponse<Boolean> modifyDistributionGoodsStatus(@RequestBody @Valid EsGoodsInfoModifyDistributionGoodsStatusRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.modifyDistributionGoodsStatus(request));
    }

    @Override
    public BaseResponse<Boolean> modifyDistributionCommission(@RequestBody @Valid EsGoodsInfoModifyDistributionCommissionRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.modifyDistributionCommission(request));
    }

    @Override
    public BaseResponse<Boolean> modifyDistributionGoodsAuditBySpuId(@RequestBody @Valid EsGoodsInfoModifyDistributionBySpuIdRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.modifyDistributionGoodsAudit(request.getSpuId()));
    }

    @Override
    public BaseResponse<Boolean> modifyEnterpriseAuditStatus(@RequestBody @Valid EsGoodsInfoEnterpriseAuditRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.modifyEnterpriseAuditStatus(request));
    }

    @Override
    public BaseResponse<Boolean> updateEnterpriseGoodsInfo(@RequestBody @Valid EsGoodsInfoEnterpriseBatchAuditRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.updateEnterpriseGoodsInfo(
                request.getBatchEnterPrisePriceDTOS(), request.getEnterpriseAuditState()));
    }

    @Override
    public BaseResponse updateAddedStatus(@RequestBody @Valid EsGoodsInfoModifyAddedStatusRequest request) {
        esGoodsInfoElasticService.updateAddedStatus(request.getAddedFlag(), request.getGoodsIds(),
                request.getGoodsInfoIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByGoods(@RequestBody @Valid EsGoodsDeleteByIdsRequest request) {
        esGoodsInfoElasticService.deleteByGoods(request.getDeleteIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initProviderEsGoodsInfo(@RequestBody @Valid EsGoodsInitProviderGoodsInfoRequest request) {
        esGoodsInfoElasticService.initProviderEsGoodsInfo(request.getStoreId(), request.getProviderGoodsIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse delete(@RequestBody @Valid EsGoodsDeleteByIdsRequest request) {
        esGoodsInfoElasticService.delete(request.getDeleteIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<Long> updateSalesNumBySpuId(@RequestBody @Valid EsGoodsModifySalesNumBySpuIdRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.updateSalesNumBySpuId(request.getSpuId(),
                request.getSalesNum()));
    }

    @Override
    public BaseResponse<Long> updateSortNoBySpuId(@RequestBody @Valid EsGoodsModifySortNoBySpuIdRequest request) {
        return BaseResponse.success(esGoodsInfoElasticService.updateSortNoBySpuId(request.getSpuId(),
                request.getSortNo()));
    }

    @Override
    public BaseResponse delStoreCateIds(@RequestBody @Valid EsGoodsDeleteStoreCateRequest request) {
        esGoodsInfoElasticService.delStoreCateIds(request.getStoreCateIds(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse adjustPrice(@RequestBody @Valid EsGoodsInfoAdjustPriceRequest request) {
        esGoodsInfoElasticService.adjustPrice(request);
        return BaseResponse.SUCCESSFUL();
    }
}
