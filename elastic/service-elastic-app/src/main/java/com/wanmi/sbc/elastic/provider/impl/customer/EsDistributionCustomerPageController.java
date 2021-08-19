package com.wanmi.sbc.elastic.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomeffBatchModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.DistributionCustomerExportResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerListResponse;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerPageResponse;
import com.wanmi.sbc.elastic.customer.service.EsDistributionCustomerQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: HouShuai
 * @date: 2020/12/7 11:26
 * @description:
 */
@RestController
public class EsDistributionCustomerPageController implements EsDistributionCustomerQueryProvider {


    @Autowired
    private EsDistributionCustomerQueryService esDistributionCustomerService;


    @Override
    public BaseResponse<EsDistributionCustomerPageResponse> page(@RequestBody @Valid EsDistributionCustomerPageRequest request) {
        EsDistributionCustomerPageResponse page = esDistributionCustomerService.page(request);
        return BaseResponse.success(page);
    }

    @Override
    public BaseResponse<EsDistributionCustomerListResponse> listByIds(@RequestBody @Valid EsDistributionCustomeffBatchModifyRequest queryRequest) {

        return esDistributionCustomerService.listByIds(queryRequest);
    }

    @Override
    public BaseResponse<DistributionCustomerExportResponse> export(@Valid EsDistributionCustomerPageRequest request) {
        return esDistributionCustomerService.export(request);
    }
}
