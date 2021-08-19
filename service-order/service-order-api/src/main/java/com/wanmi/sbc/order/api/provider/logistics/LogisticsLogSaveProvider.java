package com.wanmi.sbc.order.api.provider.logistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogNoticeForKuaidiHundredRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p> 服务信息服务</p>
 * author: dyt
 * Date: 2020-04-17
 */
@FeignClient(value = "${application.order.name}", contextId = "LogisticsLogSaveProvider")
public interface LogisticsLogSaveProvider {

    /**
     * 根据快递100的回调通知请求参数
     * @param request  {@link  LogisticsLogNoticeForKuaidiHundredRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/logistics-log/modify-for-Kuaidi-hundred")
    BaseResponse modifyForKuaidiHundred(@RequestBody LogisticsLogNoticeForKuaidiHundredRequest request);
}
