package com.soybean.mall.order.api.provider.order;


import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@FeignClient(value = "${application.order.name}", contextId = "OrderConfigProvider")
public interface OrderConfigProvider {


    /**
     * 配置信息
     * @return
     */
    @PostMapping("/order/${application.order.version}/list-config")
    BaseResponse<Map<String, String>> listConfig();
}
