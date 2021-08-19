package com.wanmi.sbc.elastic.api.provider.customerFunds;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsPageRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementPageRequest;
import com.wanmi.sbc.elastic.api.response.customerFunds.EsCustomerFundsResponse;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description // 会员资金
 * @Date 18:27 2020/12/11
 * @Param
 * @return
 **/
@FeignClient(value = "${application.elastic.name}", contextId = "EsCustomerFundsQueryProvider")
public interface EsCustomerFundsQueryProvider {


    @PostMapping("/elastic/${application.elastic.version}/Customer-funds/es-customer-funds-page")
    BaseResponse<EsCustomerFundsResponse> esCustomerFundsPage(@RequestBody @Valid EsCustomerFundsPageRequest request);




}
