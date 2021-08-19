package com.wanmi.sbc.job;

import com.wanmi.sbc.linkedmall.api.provider.address.LinkedMallAddressProvider;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressProvider;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 定时任务Handler
 * 第三方地址初始化自动映射任务
 *
 * @author dyt
 */
@JobHandler(value = "thirdAddressSyncJobHandler")
@Component
@Slf4j
public class ThirdAddressSyncJobHandler extends IJobHandler {

    @Autowired
    private LinkedMallAddressProvider linkedMallAddressProvider;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        init();
        return SUCCESS;
    }

    @Async
    public void init(){
        //处理初始化需要较长时间，会导致微服务超时，所以用异步
        ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();
        if(Objects.nonNull(response)) {
            XxlJobLogger.log("第三方地址初始化自动映射执行 " + LocalDateTime.now());
            linkedMallAddressProvider.init();
        }
    }
}
