package com.wanmi.sbc.job;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeProvider;
import com.wanmi.sbc.order.api.request.thirdplatformtrade.ThirdPlatformTradeCompensateRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务Handler（Bean模式）
 * linkedMall订单补偿定时任务
 */
@JobHandler(value = "linkedMallOrderPayJobHandler")
@Component
@Slf4j
public class LinkedMallOrderPayJobHandler extends IJobHandler {

    @Autowired
    private ThirdPlatformTradeProvider thirdPlatformTradeProvider;

    /**
     * linkedMall订单定时任务
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("linkedMall订单补偿定时任务执行");
        thirdPlatformTradeProvider.compensate(
                ThirdPlatformTradeCompensateRequest.builder().thirdPlatformType(ThirdPlatformType.LINKED_MALL).build());
        log.info("linkedMall订单补偿任务执行结束");
        return SUCCESS;
    }
}
