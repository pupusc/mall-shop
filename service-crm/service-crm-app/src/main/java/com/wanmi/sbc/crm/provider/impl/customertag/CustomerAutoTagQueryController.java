package com.wanmi.sbc.crm.provider.impl.customertag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customertag.CustomerAutoTagQueryProvider;
import com.wanmi.sbc.crm.api.request.customertag.CustomerAutoTagRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerPreferenceAutoTagRequest;
import com.wanmi.sbc.crm.customertag.service.CustomerAutoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class CustomerAutoTagQueryController implements CustomerAutoTagQueryProvider {

    @Autowired
    private CustomerAutoTagService customerAutoTagService;

    @Override
    public BaseResponse getCustomerTag(@Valid CustomerAutoTagRequest request) {
        return customerAutoTagService.getCustomerTag(request);
    }

    @Override
    public BaseResponse getCustomerPreferenceTag(@Valid CustomerPreferenceAutoTagRequest request) {
        return customerAutoTagService.getCustomerPreferenceTag(request);
    }
}
