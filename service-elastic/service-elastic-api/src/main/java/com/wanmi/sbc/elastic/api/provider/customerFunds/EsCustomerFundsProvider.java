package com.wanmi.sbc.elastic.api.provider.customerFunds;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsListRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsModifyRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description // 会员资金
 * @Date 10:27 2020/12/15
 * @Param
 * @return
 **/
@FeignClient(value = "${application.elastic.name}", contextId = "EsCustomerFundsProvider")
public interface EsCustomerFundsProvider {


    @PostMapping("/elastic/${application.elastic.version}/customer/funds/init")
    BaseResponse initCustomerFunds(@RequestBody @Valid EsCustomerFundsRequest request);

    @PostMapping("/elastic/${application.elastic.version}/customer/funds/init-customer-funds-list")
    BaseResponse initCustomerFundsList(@RequestBody @Valid EsCustomerFundsListRequest request);

    @PostMapping("/elastic/${application.elastic.version}/settlement/update-customer-funds")
    BaseResponse updateCustomerFunds(@RequestBody @Valid EsCustomerFundsModifyRequest request);




}
