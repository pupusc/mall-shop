package com.wanmi.sbc.elastic.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.coupon.EsCouponInfoProvider;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoDeleteByIdRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoInitRequest;
import com.wanmi.sbc.elastic.coupon.service.EsCouponInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class EsCouponInfoController implements EsCouponInfoProvider {

    @Autowired
    private EsCouponInfoService esCouponInfoService;

    @Override
    public BaseResponse init(@RequestBody @Valid EsCouponInfoInitRequest request) {
        esCouponInfoService.init(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse add(@RequestBody @Valid EsCouponInfoAddRequest request) {
        esCouponInfoService.save(request.getCouponInfoDTO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid EsCouponInfoDeleteByIdRequest request) {
        esCouponInfoService.deleteById(request.getCouponId());
        return BaseResponse.SUCCESSFUL();
    }
}
