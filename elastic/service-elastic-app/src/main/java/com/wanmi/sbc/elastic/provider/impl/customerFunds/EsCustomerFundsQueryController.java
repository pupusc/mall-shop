package com.wanmi.sbc.elastic.provider.impl.customerFunds;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsQueryProvider;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementQueryProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsPageRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementPageRequest;
import com.wanmi.sbc.elastic.api.response.customerFunds.EsCustomerFundsResponse;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import com.wanmi.sbc.elastic.customerFunds.service.EsCustomerFundsService;
import com.wanmi.sbc.elastic.settlement.service.EsSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description //会员资金
 * @Date 19:05 2020/12/15
 * @Param
 * @return
 **/
@RestController
@Validated
public class EsCustomerFundsQueryController implements EsCustomerFundsQueryProvider {

    @Autowired
    private EsCustomerFundsService esCustomerFundsService;




    @Override
    public BaseResponse<EsCustomerFundsResponse> esCustomerFundsPage(@Valid EsCustomerFundsPageRequest request) {
        return BaseResponse.success(esCustomerFundsService.queryCustomerFundsPage(request));
    }
}
