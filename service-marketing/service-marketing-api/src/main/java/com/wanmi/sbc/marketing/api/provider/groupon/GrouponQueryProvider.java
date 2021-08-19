package com.wanmi.sbc.marketing.api.provider.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.groupon.GrouponCheckRequest;
import com.wanmi.sbc.marketing.api.response.groupon.GrouponCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>C端拼团业务Provider</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponQueryProvider")
public interface GrouponQueryProvider {
   //todo查询活动列表
    //查询团信息
    //查询参团信息
    //验证团状态
    /**
     * 团状态验证
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/check")
    BaseResponse<GrouponCheckResponse> grouponCheck(
            @RequestBody @Valid GrouponCheckRequest request) ;


}

