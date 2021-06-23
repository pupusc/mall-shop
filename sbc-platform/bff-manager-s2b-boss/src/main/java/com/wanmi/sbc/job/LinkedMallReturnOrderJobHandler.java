package com.wanmi.sbc.job;

import com.wanmi.sbc.order.api.provider.linkedmall.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 定时任务Handler（Bean模式）
 * linkedMall退单定时任务
 */
@JobHandler(value = "LinkedMallReturnOrderJobHandler")
@Component
@Slf4j
public class LinkedMallReturnOrderJobHandler extends IJobHandler {

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private LinkedMallReturnOrderProvider linkedMallReturnOrderProvider;

    /**
     * linkedMall订单定时任务
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build())
                .getContext();
        if(Objects.isNull(response)){
            return SUCCESS;
        }
        XxlJobLogger.log("linkedmall退单定时任务执行 " + LocalDateTime.now());
        long start = System.currentTimeMillis();
        log.info("linkedmall退单定时任务执行 " + LocalDateTime.now());
        linkedMallReturnOrderProvider.syncStatus();
        log.info("linkedmall退单定时任务执行，耗时：{}ms ", (System.currentTimeMillis() - start));
        return SUCCESS;
    }
}
