package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.ConfigStatusModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.GrowthValueBasicRuleModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "GrowthValueBasicRuleProvider")
public interface GrowthValueBasicRuleProvider {
    /**
     * 修改成长值基础获取规则
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/growth-value/modify-growth-value-basic-rule")
    BaseResponse modifyGrowthValueBasicRule(@RequestBody GrowthValueBasicRuleModifyRequest request);
}
