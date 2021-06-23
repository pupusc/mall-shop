package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateModifyResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateSortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>优惠券分类操作接口</p>
 * author: sunkun
 * Date: 2018-11-23
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCateProvider")
public interface CouponCateProvider {

    /**
     * 新增优惠券分类
     * @param request 新增优惠券分类请求结构 {@link CouponCateAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/add")
    BaseResponse add(@RequestBody @Valid CouponCateAddRequest request);

    /**
     * 修改优惠券分类
     * @param request 修改优惠券分类请求结构 {@link CouponCateModifyRequest}
     * @return {@link CouponCateModifyResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/modify")
    BaseResponse<CouponCateModifyResponse> modify(@RequestBody @Valid CouponCateModifyRequest request);

    /**
     * 删除优惠券分类
     * @param request 删除优惠券分类请求结构 {@link CouponCateDeleteRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/delete")
    BaseResponse delete(@RequestBody @Valid CouponCateDeleteRequest request);

    /**
     * 优惠券分类排序
     * @param request 优惠券分类排序请求结构 {@link CouponCateSortRequest}
     * @return {CouponCateSortResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/sort")
    BaseResponse<CouponCateSortResponse> sort(@RequestBody @Valid CouponCateSortRequest request);


    /**
     * 优惠券分类设置只供平台可用
     * @param request 优惠券分类设置只供平台可用请求结构 {@link CouponCateIsOnlyPlatformRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/is-only-platform")
    BaseResponse isOnlyPlatform(@RequestBody @Valid CouponCateIsOnlyPlatformRequest request);
}
