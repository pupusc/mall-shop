package com.wanmi.sbc.elastic.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelPageRequest;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.request.customer.*;
import com.wanmi.sbc.elastic.customer.service.EsCustomerDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Validated
public class EsCustomerDetailController implements EsCustomerDetailProvider {

    @Autowired
    private EsCustomerDetailService esCustomerDetailService;

    @Override
    public BaseResponse init(@RequestBody @Valid EsCustomerDetailInitRequest request) {
        esCustomerDetailService.init(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse deleteCustomer(@RequestBody @Valid EsCustomerDetailInitRequest request) {
        esCustomerDetailService.deleteCustomer(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse initPaidMembers(@RequestBody @Valid EsCustomerDetailInitRequest request) {
        PaidCardCustomerRelPageRequest paidCardCustomerRelPageRequest=new PaidCardCustomerRelPageRequest();
        paidCardCustomerRelPageRequest.setPageNum(request.getPageNum());
        paidCardCustomerRelPageRequest.setPageSize(request.getPageSize());
        if(StringUtils.isNotBlank(request.getCustomerId())) {
            paidCardCustomerRelPageRequest.setCustomerId(request.getCustomerId());
        }
        esCustomerDetailService.initPaidMembers(paidCardCustomerRelPageRequest);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse add(@RequestBody @Valid EsCustomerDetailAddRequest request) {
        esCustomerDetailService.save(request.getEsCustomerDetailDTO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modify(@Valid EsCustomerDetailModifyRequest request) {
        esCustomerDetailService.modify(request.getEsCustomerDetailDTO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyCustomerStateByCustomerId(@RequestBody @Valid EsCustomerStateBatchModifyRequest request) {
        esCustomerDetailService.modifyCustomerStateByCustomerId(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyCustomerCheckState(@RequestBody @Valid EsCustomerCheckStateModifyRequest request){
        esCustomerDetailService.modifyCustomerCheckState(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse addPlatformRelated(@RequestBody @Valid EsStoreCustomerRelaAddRequest request) {
        esCustomerDetailService.addPlatformRelated(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deletePlatformRelated(@RequestBody @Valid EsStoreCustomerRelaDeleteRequest request) {
        esCustomerDetailService.deletePlatformRelated(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyByCustomerId(@RequestBody @Valid EsStoreCustomerRelaUpdateRequest request) {
        esCustomerDetailService.modifyByCustomerId(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse resetEs(@RequestBody BaseQueryRequest req) {
        esCustomerDetailService.resetEs(req);
        return BaseResponse.SUCCESSFUL();
    }
}
