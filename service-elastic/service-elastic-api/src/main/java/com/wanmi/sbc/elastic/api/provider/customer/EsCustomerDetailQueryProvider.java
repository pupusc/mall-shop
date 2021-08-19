package com.wanmi.sbc.elastic.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageTwoRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsCustomerDetailPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员详情
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCustomerDetailQueryProvider")
public interface EsCustomerDetailQueryProvider {


    /**
     * 分页查询会员
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/page")
    BaseResponse<EsCustomerDetailPageResponse> page(@RequestBody @Valid EsCustomerDetailPageRequest request);

    /**
     * 分页查询会员
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/customer-detail/page-for-enterprise-customer")
    BaseResponse<EsCustomerDetailPageResponse> pageForEnterpriseCustomer(@RequestBody @Valid EsCustomerDetailPageTwoRequest request);


}
