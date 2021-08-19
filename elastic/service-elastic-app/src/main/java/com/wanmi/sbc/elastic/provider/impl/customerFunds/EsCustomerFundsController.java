package com.wanmi.sbc.elastic.provider.impl.customerFunds;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsListRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsModifyRequest;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import com.wanmi.sbc.elastic.customerFunds.service.EsCustomerFundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description //会员资金
 * @Date 10:22 2020/12/15
 * @Param
 * @return
 **/
@RestController
@Validated
public class EsCustomerFundsController implements EsCustomerFundsProvider {

    @Autowired
    private EsCustomerFundsService esCustomerFundsService;

    @Override
    public BaseResponse initCustomerFunds(@RequestBody @Valid EsCustomerFundsRequest request) {
        esCustomerFundsService.initCustomerFunds(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initCustomerFundsList(@RequestBody @Valid EsCustomerFundsListRequest request) {
        esCustomerFundsService.initCustomerFundsList(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse updateCustomerFunds(@RequestBody @Valid EsCustomerFundsModifyRequest request) {
        esCustomerFundsService.updateCustomerFunds(request);
        return BaseResponse.SUCCESSFUL();
    }
}
