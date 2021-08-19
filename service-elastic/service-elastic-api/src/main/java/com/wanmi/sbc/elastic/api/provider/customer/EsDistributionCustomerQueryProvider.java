package com.wanmi.sbc.elastic.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomeffBatchModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.DistributionCustomerExportResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerListResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author  HouShuai
 * @date  2020/12/7 10:46
 * @description
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsDistributionCustomerQueryProvider")
public interface EsDistributionCustomerQueryProvider {


    /**
     * 分销员列表分页查询
     * @param distributionCustomerPageRequest
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/distribution-customer/page")
    BaseResponse<EsDistributionCustomerPageResponse> page(@RequestBody @Valid EsDistributionCustomerPageRequest distributionCustomerPageRequest);
    /**
     * 根据分销员ids，查询分销员信息
     *
     * @param queryRequest
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/distribution-customer/list-by-ids")
    BaseResponse<EsDistributionCustomerListResponse> listByIds(@RequestBody EsDistributionCustomeffBatchModifyRequest queryRequest);


    /**
     * 导出
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/distribution-customer/export")
    BaseResponse<DistributionCustomerExportResponse> export(@RequestBody @Valid EsDistributionCustomerPageRequest request);

}
