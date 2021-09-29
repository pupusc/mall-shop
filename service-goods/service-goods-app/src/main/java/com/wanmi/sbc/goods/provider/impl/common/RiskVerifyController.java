package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RiskVerifyController implements RiskVerifyProvider {

    @Override
    public BaseResponse verifyImage(ImageVerifyRequest imageVerifyRequest) {
        return null;
    }
}
