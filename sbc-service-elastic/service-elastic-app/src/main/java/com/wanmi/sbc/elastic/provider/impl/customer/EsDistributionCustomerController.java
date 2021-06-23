package com.wanmi.sbc.elastic.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.customer.EsDistributionCustomerProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerAddResponse;
import com.wanmi.sbc.elastic.customer.service.EsDistributionCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: HouShuai
 * @date: 2020/12/8 17:03
 * @description:
 */
@RestController
public class EsDistributionCustomerController implements EsDistributionCustomerProvider {

    @Autowired
    private EsDistributionCustomerService esDistributionCustomerService;

    @Override
    public BaseResponse<EsDistributionCustomerAddResponse> add(@RequestBody @Valid EsDistributionCustomerAddRequest request) {
        return esDistributionCustomerService.add(request);
    }

    @Override
    public BaseResponse init(EsDistributionCustomerPageRequest request) {
        esDistributionCustomerService.init(request);
        return BaseResponse.SUCCESSFUL();
    }
}
