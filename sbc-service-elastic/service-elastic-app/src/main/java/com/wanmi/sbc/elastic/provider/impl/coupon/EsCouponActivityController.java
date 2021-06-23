package com.wanmi.sbc.elastic.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.coupon.*;
import com.wanmi.sbc.elastic.coupon.service.EsCouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EsCouponActivityController implements EsCouponActivityProvider {

    @Autowired
    private EsCouponActivityService esCouponActivityService;

    @Override
    public BaseResponse init(@RequestBody @Valid EsCouponActivityInitRequest request) {
        esCouponActivityService.init(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse add(@RequestBody @Valid EsCouponActivityAddRequest request) {
        esCouponActivityService.save(request.getEsCouponActivityDTO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid EsCouponActivityDeleteByIdRequest request) {
        esCouponActivityService.deleteById(request.getActivityId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse start(@Valid EsCouponActivityStartByIdRequest request) {
        esCouponActivityService.start(request.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse pause(@Valid EsCouponActivityPauseByIdRequest request) {
        esCouponActivityService.pause(request.getId());
        return BaseResponse.SUCCESSFUL();
    }
}
