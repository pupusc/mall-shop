package com.wanmi.sbc.linkedmall.api.provider.signature;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.signature.SignatureVerifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * linkedmall验签
 */
@FeignClient(value = "${application.linkedmall.name}" ,contextId = "SignatureProvider")
public interface SignatureProvider {
    /**
     * linkedmall验签
     * @param request
     * @return
     */
    @PostMapping("linkedmall/${application.linkedmall.version}/signature/verify")
    public BaseResponse<Boolean> verifySignature(@RequestBody SignatureVerifyRequest request);
}
