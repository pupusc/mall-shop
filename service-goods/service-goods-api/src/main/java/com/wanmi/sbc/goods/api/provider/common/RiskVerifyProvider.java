package com.wanmi.sbc.goods.api.provider.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.goods.name}", contextId = "RiskVerifyProvider")
public interface RiskVerifyProvider {
    /**
     * 图片审核callback
     * @param imageVerifyRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image-audit")
    BaseResponse verifyImage(@RequestBody @Valid ImageVerifyRequest imageVerifyRequest);

}