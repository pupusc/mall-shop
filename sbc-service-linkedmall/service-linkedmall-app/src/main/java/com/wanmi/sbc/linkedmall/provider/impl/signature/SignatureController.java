package com.wanmi.sbc.linkedmall.provider.impl.signature;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.provider.signature.SignatureProvider;
import com.wanmi.sbc.linkedmall.api.request.signature.SignatureVerifyRequest;
import com.wanmi.sbc.linkedmall.signature.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * linkedmall验签
 */
@RestController
public class SignatureController implements SignatureProvider {
    @Autowired
    private SignatureService signatureService;
    @Override
    public BaseResponse<Boolean> verifySignature(SignatureVerifyRequest request) {
        return BaseResponse.success(signatureService.verify(request));
    }
}
