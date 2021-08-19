package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoCopyByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoDeleteByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoModifyRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoAddResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoCopyByIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoModifyResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.coupon.service.CouponInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对优惠券操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@Validated
@RestController
public class CouponInfoController implements CouponInfoProvider {

    @Autowired
    private CouponInfoService couponInfoService;

    /**
     * 新增优惠券
     *
     * @param request 优惠券新增信息结构 {@link CouponInfoAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse<CouponInfoAddResponse> add(@RequestBody @Valid CouponInfoAddRequest request){
        CouponInfoVO couponInfoVO = couponInfoService.addCouponInfo(request);
        return BaseResponse.success(new CouponInfoAddResponse(couponInfoVO));
    }

    /**
     * 修改优惠券
     *
     * @param request 优惠券修改信息结构 {@link CouponInfoModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse<CouponInfoModifyResponse> modify(@RequestBody @Valid CouponInfoModifyRequest request){
        CouponInfoVO couponInfoVO = couponInfoService.modifyCouponInfo(request);
        return BaseResponse.success(new CouponInfoModifyResponse(couponInfoVO));
    }

    /**
     * 根据id删除优惠券
     *
     * @param request 包含id的删除请求结构 {@link CouponInfoDeleteByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse deleteById(@RequestBody @Valid CouponInfoDeleteByIdRequest request){
        couponInfoService.deleteCoupon(request.getCouponId(), request.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据id复制优惠券
     *
     * @param request 包含id的复制请求结构 {@link CouponInfoCopyByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse<CouponInfoCopyByIdResponse> copyById(@RequestBody @Valid CouponInfoCopyByIdRequest request){
        CouponInfoVO couponInfoVO = couponInfoService.copyCouponInfo(request.getCouponId(), request.getOperatorId());
        return BaseResponse.success(new CouponInfoCopyByIdResponse(couponInfoVO));
    }
}
