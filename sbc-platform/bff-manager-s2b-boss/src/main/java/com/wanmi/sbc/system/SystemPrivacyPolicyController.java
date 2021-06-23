package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.systemprivacypolicy.SystemPrivacyPolicyQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemprivacypolicy.SystemPrivacyPolicySaveProvider;
import com.wanmi.sbc.setting.api.request.systemprivacypolicy.*;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.*;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Api(description = "隐私政策管理API", tags = "SystemPrivacyPolicyController")
@RestController
@RequestMapping(value = "/boss/systemprivacypolicy")
public class SystemPrivacyPolicyController {

    @Autowired
    private SystemPrivacyPolicyQueryProvider systemPrivacyPolicyQueryProvider;

    @Autowired
    private SystemPrivacyPolicySaveProvider systemPrivacyPolicySaveProvider;

    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "查询隐私政策")
    @GetMapping
    public BaseResponse<SystemPrivacyPolicyResponse> query() {
        return systemPrivacyPolicyQueryProvider.querySystemPrivacyPolicy();
    }


    @ApiOperation(value = "编辑/新增隐私政策")
    @PostMapping
    public BaseResponse modify(@RequestBody @Valid SystemPrivacyPolicyRequest modifyReq) {
        modifyReq.setOperator(commonUtil.getOperator());
        return systemPrivacyPolicySaveProvider.modifyOrAdd(modifyReq);
    }


}
