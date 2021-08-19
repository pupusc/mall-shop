package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "${application.setting.name}", contextId = "InitProvider")
public interface InitProvider {

    /**
     * 根据type和key更新status，如果是商品审核关闭，会同步关闭自营商品审核开关
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/init")
    BaseResponse init();

   }
