package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import com.wanmi.sbc.goods.common.RiskVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RiskVerifyController implements RiskVerifyProvider {

    @Autowired
    private RiskVerifyService riskVerifyService;

    @Override
    public BaseResponse verifyImageCallBack(ImageVerifyRequest imageVerifyRequest) {
        if (imageVerifyRequest == null || StringUtils.isEmpty(imageVerifyRequest.getRequestId())) {
            throw new SbcRuntimeException("K-000001");
        }
        riskVerifyService.verifyImageCallBack(imageVerifyRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse verifyImage() {
        riskVerifyService.verifyImage();
        return BaseResponse.SUCCESSFUL();
    }
}
