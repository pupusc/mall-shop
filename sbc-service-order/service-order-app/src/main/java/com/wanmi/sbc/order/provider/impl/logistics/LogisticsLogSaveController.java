package com.wanmi.sbc.order.provider.impl.logistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.logistics.LogisticsLogSaveProvider;
import com.wanmi.sbc.order.api.request.logistics.LogisticsLogNoticeForKuaidiHundredRequest;
import com.wanmi.sbc.order.logistics.service.LogisticsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>服务信息服务</p>
 */
@RestController
public class LogisticsLogSaveController implements LogisticsLogSaveProvider {

    @Autowired
    private LogisticsLogService logisticsLogService;

    @Override
    public BaseResponse modifyForKuaidiHundred(@RequestBody LogisticsLogNoticeForKuaidiHundredRequest request) {
        logisticsLogService.modifyForKuaiDi100(request.getKuaidiHundredNoticeDTO());
        return BaseResponse.SUCCESSFUL();
    }
}
