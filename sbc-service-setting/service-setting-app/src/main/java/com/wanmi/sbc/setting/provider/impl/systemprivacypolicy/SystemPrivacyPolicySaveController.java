package com.wanmi.sbc.setting.provider.impl.systemprivacypolicy;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.systemprivacypolicy.*;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemprivacypolicy.SystemPrivacyPolicySaveProvider;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyAddResponse;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyModifyResponse;
import com.wanmi.sbc.setting.systemprivacypolicy.service.SystemPrivacyPolicyService;
import com.wanmi.sbc.setting.systemprivacypolicy.model.root.SystemPrivacyPolicy;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * <p>隐私政策保存服务接口实现</p>
 *
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@RestController
@Validated
public class SystemPrivacyPolicySaveController implements SystemPrivacyPolicySaveProvider {
    @Autowired
    private SystemPrivacyPolicyService systemPrivacyPolicyService;


    @Override
    public BaseResponse<SystemPrivacyPolicyAddResponse> add(@RequestBody @Valid SystemPrivacyPolicyAddRequest systemPrivacyPolicyAddRequest) {
        SystemPrivacyPolicy systemPrivacyPolicy = new SystemPrivacyPolicy();
        KsBeanUtil.copyPropertiesThird(systemPrivacyPolicyAddRequest, systemPrivacyPolicy);
        return BaseResponse.success(new SystemPrivacyPolicyAddResponse(
                systemPrivacyPolicyService.wrapperVo(systemPrivacyPolicyService.add(systemPrivacyPolicy))));
    }

    @Override
    public BaseResponse<SystemPrivacyPolicyModifyResponse> modify(@RequestBody @Valid SystemPrivacyPolicyModifyRequest systemPrivacyPolicyModifyRequest) {
        SystemPrivacyPolicy systemPrivacyPolicy = new SystemPrivacyPolicy();
        KsBeanUtil.copyPropertiesThird(systemPrivacyPolicyModifyRequest, systemPrivacyPolicy);
        return BaseResponse.success(new SystemPrivacyPolicyModifyResponse(
                systemPrivacyPolicyService.wrapperVo(systemPrivacyPolicyService.modify(systemPrivacyPolicy))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid SystemPrivacyPolicyDelByIdRequest systemPrivacyPolicyDelByIdRequest) {
        systemPrivacyPolicyService.deleteById(systemPrivacyPolicyDelByIdRequest.getPrivacyPolicyId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid SystemPrivacyPolicyDelByIdListRequest systemPrivacyPolicyDelByIdListRequest) {
        systemPrivacyPolicyService.deleteByIdList(systemPrivacyPolicyDelByIdListRequest.getPrivacyPolicyIdList());
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse modifyOrAdd(@RequestBody @Valid SystemPrivacyPolicyRequest systemPrivacyPolicyRequest) {
        SystemPrivacyPolicy systemPrivacyPolicy = new SystemPrivacyPolicy();
        if (StringUtils.isNotEmpty(systemPrivacyPolicyRequest.getPrivacyPolicyId())) {
            systemPrivacyPolicy = systemPrivacyPolicyService.getById(systemPrivacyPolicyRequest.getPrivacyPolicyId());
        }
        KsBeanUtil.copyProperties(systemPrivacyPolicyRequest, systemPrivacyPolicy);
		systemPrivacyPolicy.setDelFlag(DeleteFlag.NO);
        if (StringUtils.isBlank(systemPrivacyPolicyRequest.getPrivacyPolicyId())) {
            systemPrivacyPolicy.setCreatePerson(systemPrivacyPolicyRequest.getOperator().getUserId());
            systemPrivacyPolicy.setCreateTime(LocalDateTime.now());
        } else {
            systemPrivacyPolicy.setUpdatePerson(systemPrivacyPolicyRequest.getOperator().getUserId());
            systemPrivacyPolicy.setUpdateTime(LocalDateTime.now());
        }
        systemPrivacyPolicyService.modify(systemPrivacyPolicy);
        return BaseResponse.SUCCESSFUL();
    }

}

