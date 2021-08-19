package com.wanmi.sbc.marketing.provider.impl.pointscoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponSaveProvider;
import com.wanmi.sbc.marketing.api.request.pointscoupon.*;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponAddListResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponAddResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponModifyResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponSendCodeResponse;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import com.wanmi.sbc.marketing.pointscoupon.service.PointsCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>积分兑换券表保存服务接口实现</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@RestController
@Validated
public class PointsCouponSaveController implements PointsCouponSaveProvider {
    @Autowired
    private PointsCouponService pointsCouponService;

    @Override
    public BaseResponse<PointsCouponAddResponse> add(@RequestBody @Valid PointsCouponAddRequest pointsCouponAddRequest) {
        PointsCoupon pointsCoupon = new PointsCoupon();
        KsBeanUtil.copyPropertiesThird(pointsCouponAddRequest, pointsCoupon);
        return BaseResponse.success(new PointsCouponAddResponse(
                pointsCouponService.wrapperVo(pointsCouponService.add(pointsCoupon))));
    }

    @Override
    public BaseResponse<PointsCouponAddListResponse> batchAdd(@RequestBody @Valid PointsCouponAddListRequest pointsCouponAddListRequest) {
        List<PointsCouponAddRequest> pointsCouponAddRequestList = pointsCouponAddListRequest.getPointsCouponAddRequestList();
        List pointsCouponList = KsBeanUtil.copyListProperties(pointsCouponAddRequestList, PointsCoupon.class);
        List<String> activityIdList = pointsCouponService.batchAdd(pointsCouponList);
        return BaseResponse.success(new PointsCouponAddListResponse(activityIdList));
    }

    @Override
    public BaseResponse<PointsCouponModifyResponse> modify(@RequestBody @Valid PointsCouponModifyRequest pointsCouponModifyRequest) {
        PointsCoupon pointsCoupon = new PointsCoupon();
        KsBeanUtil.copyPropertiesThird(pointsCouponModifyRequest, pointsCoupon);

        return BaseResponse.success(new PointsCouponModifyResponse(
                pointsCouponService.wrapperVo(pointsCouponService.modify(pointsCoupon))));
    }

    @Override
    public BaseResponse modifyStatus(@RequestBody @Valid PointsCouponSwitchRequest request) {
        PointsCoupon pointsCoupon = pointsCouponService.getById(request.getPointsCouponId());
        pointsCoupon.setStatus(request.getStatus());
        pointsCoupon.setUpdateTime(request.getUpdateTime());
        pointsCoupon.setUpdatePerson(request.getUpdatePerson());
        pointsCouponService.modifyStatus(pointsCoupon);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid PointsCouponDelByIdRequest pointsCouponDelByIdRequest) {
        pointsCouponService.deleteById(pointsCouponDelByIdRequest.getPointsCouponId(), pointsCouponDelByIdRequest.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<PointsCouponSendCodeResponse> exchangePointsCoupon(@RequestBody @Valid PointsCouponFetchRequest pointsCouponFetchRequest) {
        return BaseResponse.success(pointsCouponService.sendCouponCode(pointsCouponFetchRequest));
    }

}

