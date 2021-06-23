package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.PointsBasicRuleListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "${application.setting.name}", contextId = "PointsBasicRuleQueryProvider")
public interface PointsBasicRuleQueryProvider {
    /**
     * 查询积分基础获取规则
     * @return {@link PointsBasicRuleListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/points/list-points-basic-rule")
    BaseResponse<PointsBasicRuleListResponse> listPointsBasicRule();
}
