package com.wanmi.sbc.order.api.provider.settlement;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.settlement.SettlementAnalyseRequest;
import com.wanmi.sbc.order.api.response.settlement.SettlementForEsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-07 13:49
 */
@FeignClient(value = "${application.order.name}", contextId = "SettlementAnalyseProvider")
public interface SettlementAnalyseProvider {

    /**
     * 分析结算单
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/settlement/analyse")
    BaseResponse<SettlementForEsResponse> analyse(@RequestBody @Valid SettlementAnalyseRequest request);

}
