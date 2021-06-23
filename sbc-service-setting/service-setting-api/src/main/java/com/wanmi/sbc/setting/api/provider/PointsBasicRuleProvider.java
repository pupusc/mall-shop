package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.PointsBasicRuleModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "PointsBasicRuleProvider")
public interface PointsBasicRuleProvider {
    /**
     * 修改积分基础获取规则
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/points/modify-points-basic-rule")
    BaseResponse modifyPointsBasicRule(@RequestBody PointsBasicRuleModifyRequest request);
}
