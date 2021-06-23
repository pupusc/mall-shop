package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCateProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateModifyResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateSortResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponCateSortVO;
import com.wanmi.sbc.marketing.coupon.service.CouponCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-24
 */
@Validated
@RestController
public class CouponCateController implements CouponCateProvider {

    @Autowired
    private CouponCateService couponCateService;

    /**
     * 新增优惠券分类
     * @param request 新增优惠券分类请求结构 {@link CouponCateAddRequest}
     * @return
     */
    @Override
    public BaseResponse add(@RequestBody @Valid CouponCateAddRequest request) {
        couponCateService.addCouponCate(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改优惠券分类
     * @param request 修改优惠券分类请求结构 {@link CouponCateModifyRequest}
     * @return
     */
    @Override
    public BaseResponse<CouponCateModifyResponse> modify(@RequestBody @Valid CouponCateModifyRequest request) {
        couponCateService.modifyCouponCate(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除优惠券分类
     * @param request 删除优惠券分类请求结构 {@link CouponCateDeleteRequest}
     * @return
     */
    @Override
    public BaseResponse delete(@RequestBody @Valid CouponCateDeleteRequest request) {
        couponCateService.deleteCouponCateByCouponCateId(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 优惠券分类排序
     * @param request 优惠券分类排序请求结构 {@link CouponCateSortRequest}
     * @return
     */
    @Override
    public BaseResponse<CouponCateSortResponse> sort(@RequestBody @Valid CouponCateSortRequest request) {
        List<CouponCateSortVO> couponCateSortVOS = couponCateService.sortCouponCate(KsBeanUtil.convertList(request.getList(),
                com.wanmi.sbc.marketing.coupon.request.CouponCateSortRequest.class));
        return BaseResponse.success(new CouponCateSortResponse(couponCateSortVOS));
    }

    /**
     * 优惠券分类设置只供平台可用
     * @param request 优惠券分类设置只供平台可用请求结构 {@link CouponCateIsOnlyPlatformRequest}
     * @return
     */
    @Override
    public BaseResponse isOnlyPlatform(@RequestBody @Valid CouponCateIsOnlyPlatformRequest request) {
        couponCateService.isOnlyPlatform(request);
        return BaseResponse.SUCCESSFUL();
    }
}
