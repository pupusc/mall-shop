package com.wanmi.sbc.crm.customertag.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.customertag.CustomerAutoTagRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerPreferenceAutoTagRequest;
import com.wanmi.sbc.dw.api.provider.DWCustomerTagProvider;
import com.wanmi.sbc.dw.api.request.CustomerPreferenceTagRequest;
import com.wanmi.sbc.dw.api.request.CustomerTagRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAutoTagService {
    @Autowired
    private DWCustomerTagProvider dwCustomerTagProvider;

    public BaseResponse getCustomerTag( CustomerAutoTagRequest request) {
        CustomerTagRequest customerTagRequest = KsBeanUtil.convert(request,CustomerTagRequest.class);
        return BaseResponse.success(dwCustomerTagProvider.getCustomerTag(customerTagRequest).getContext());
    }

    public BaseResponse getCustomerPreferenceTag(CustomerPreferenceAutoTagRequest request) {

        CustomerPreferenceTagRequest customerPreferenceTagRequest = KsBeanUtil.convert(request,CustomerPreferenceTagRequest.class);
        return BaseResponse.success(dwCustomerTagProvider.getCustomerPreferenceTag(customerPreferenceTagRequest).getContext());
    }
}
