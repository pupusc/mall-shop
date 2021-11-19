package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsInfoElasticProvider")
public interface EsGoodsInfoElasticProvider {

    @PostMapping("/elastic/${application.elastic.version}/goods/delete-by-brand-ids")
    BaseResponse delBrandIds(@RequestBody @Valid EsBrandDeleteByIdsRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/init")
    BaseResponse initEsGoodsInfo(@RequestBody @Valid EsGoodsInfoRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/setExtProp")
    BaseResponse setExtPropForGoods(@RequestBody List<Object[]> props);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/distribution/goods/audit")
    BaseResponse<Boolean> modifyDistributionGoodsAudit(@RequestBody @Valid EsGoodsInfoModifyDistributionGoodsAuditRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/distribution/goods/status")
    BaseResponse<Boolean> modifyDistributionGoodsStatus(@RequestBody @Valid EsGoodsInfoModifyDistributionGoodsStatusRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/distribution/commission")
    BaseResponse<Boolean> modifyDistributionCommission(@RequestBody @Valid EsGoodsInfoModifyDistributionCommissionRequest esGoodsInfoDistributionRequest);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/distribution/goods/by/spu/id")
    BaseResponse<Boolean> modifyDistributionGoodsAuditBySpuId(@RequestBody @Valid EsGoodsInfoModifyDistributionBySpuIdRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/enterprise/audit/status")
    BaseResponse<Boolean> modifyEnterpriseAuditStatus(@RequestBody @Valid EsGoodsInfoEnterpriseAuditRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/enterprise/batch/audit/status")
    BaseResponse<Boolean> updateEnterpriseGoodsInfo(@RequestBody @Valid EsGoodsInfoEnterpriseBatchAuditRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/modify/added/status")
    BaseResponse updateAddedStatus(@RequestBody @Valid EsGoodsInfoModifyAddedStatusRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/delete/goods/ids")
    BaseResponse deleteByGoods(@RequestBody @Valid EsGoodsDeleteByIdsRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/init/provider/info")
    BaseResponse initProviderEsGoodsInfo(@RequestBody @Valid EsGoodsInitProviderGoodsInfoRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/delete/by/sku/ids")
    BaseResponse delete(@RequestBody @Valid EsGoodsDeleteByIdsRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/update/sales/num/by/spu/id")
    BaseResponse<Long> updateSalesNumBySpuId(@RequestBody @Valid EsGoodsModifySalesNumBySpuIdRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/update/sort/no/by/spu/id")
    BaseResponse<Long> updateSortNoBySpuId(@RequestBody @Valid EsGoodsModifySortNoBySpuIdRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/delete/store/cate")
    BaseResponse delStoreCateIds(@RequestBody @Valid EsGoodsDeleteStoreCateRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/update/classify")
    BaseResponse updateClassify(@RequestParam("id") Integer id, @RequestParam("name") String name);

    @PostMapping("/elastic/${application.elastic.version}/goods/delete/classify")
    BaseResponse delClassify(@RequestParam("id") Integer id);

    @PostMapping("/elastic/${application.elastic.version}/goods/adjust/price")
    BaseResponse adjustPrice(@RequestBody @Valid EsGoodsInfoAdjustPriceRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/adjust/atmosphere")
    BaseResponse adjustAtmosphere(@RequestBody @Valid EsGoodsAtmosphereRequest request);
}
