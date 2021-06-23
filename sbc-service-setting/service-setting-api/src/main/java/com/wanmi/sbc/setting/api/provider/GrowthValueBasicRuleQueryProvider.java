package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.GrowthValueBasicRuleListResponse;
import com.wanmi.sbc.setting.api.response.GrowthValueBasicRuleResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "GrowthValueBasicRuleQueryProvider")
public interface GrowthValueBasicRuleQueryProvider {
    /**
     * 查询成长值获取基础规则
     * @return {@link GrowthValueBasicRuleListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/growth-value/list-growth-value-basic-rule")
    BaseResponse<GrowthValueBasicRuleListResponse> listGrowthValueBasicRule();

    /**
     * 根据类型查询成长值规则
     * @return {@link GrowthValueBasicRuleListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/growth-value/query-by-config-type")
    BaseResponse<GrowthValueBasicRuleResponse> findGrowthValueByConfigType(@RequestBody ConfigType type);
}
