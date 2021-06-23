package com.wanmi.sbc.elastic.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerAddRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsDistributionCustomerAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author  HouShuai
 * @date  2020/12/7 10:46
 * @description
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsDistributionCustomerProvider")
public interface EsDistributionCustomerProvider {


    /**
     * 分销员列表分页查询
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/distribution-customer/add")
    BaseResponse<EsDistributionCustomerAddResponse> add(@RequestBody @Valid EsDistributionCustomerAddRequest request);

    /**
     * 初始化分销员列表数据
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/distribution-customer/init")
    BaseResponse init(EsDistributionCustomerPageRequest request);

}
