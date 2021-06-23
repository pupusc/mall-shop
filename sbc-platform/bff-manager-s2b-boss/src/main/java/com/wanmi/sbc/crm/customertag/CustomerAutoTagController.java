package com.wanmi.sbc.crm.customertag;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customertag.CustomerAutoTagQueryProvider;
import com.wanmi.sbc.crm.api.request.customertag.CustomerAutoTagRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerPreferenceAutoTagRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(description = "会员自动标签管理API", tags = "CustomerAutoTagController")
@RestController
@RequestMapping(value = "/customerAutoTag")
public class CustomerAutoTagController {

    @Autowired
    private CustomerAutoTagQueryProvider customerAutoTagQueryProvider;


    @PostMapping("/tag")
    public BaseResponse getCustomerTag(@RequestBody @Valid CustomerAutoTagRequest request){
        return customerAutoTagQueryProvider.getCustomerTag(request);
    }

    /*
     * 获取会员偏好类标签
     * @param request
     * @return
     **/
    @PostMapping("/preferenceTag")
    public BaseResponse getCustomerPreferenceTag(@RequestBody @Valid CustomerPreferenceAutoTagRequest request){
        return customerAutoTagQueryProvider.getCustomerPreferenceTag(request);
    }
}
