package com.soybean.mall.order.api.provider.order;


import com.soybean.mall.order.api.request.process.AppIdProcessReq;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "${application.order.name}", contextId = "ProcessDateProvider")
public interface ProcessDateProvider {

    /**
     *  商户号信息
     * @return
     */
    @PostMapping("/order/${application.order.version}/process/appId")
    BaseResponse processAppId(@RequestBody AppIdProcessReq appIdProcessReq);
}
