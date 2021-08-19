package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.systemprivacypolicy.SystemPrivacyPolicyQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemprivacypolicy.SystemPrivacyPolicySaveProvider;
import com.wanmi.sbc.setting.api.request.systemprivacypolicy.SystemPrivacyPolicyRequest;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "隐私政策管理API", tags = "SystemPrivacyPolicyController")
@RestController
@RequestMapping(value = "/privacypolicy/queryPrivacyPolicy")
public class SystemPrivacyPolicyController {

    @Autowired
    private SystemPrivacyPolicyQueryProvider systemPrivacyPolicyQueryProvider;


    @ApiOperation(value = "查询隐私政策")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<SystemPrivacyPolicyResponse> query() {
        return systemPrivacyPolicyQueryProvider.querySystemPrivacyPolicy();
    }



}
