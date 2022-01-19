package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheCenterPageResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsDetailResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsListResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponGoodsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 10:19
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCacheProvider")
public interface CouponCacheProvider {

    /**
     * 领券中心 - 查询正在进行的优惠券活动，暂时全部查询，不带任何条件，领券的时候做判断
     * @param request 分页请求参数 {@link CouponCacheCenterPageRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cache/page")
    BaseResponse<CouponCacheCenterPageResponse> pageCouponStarted(@RequestBody @Valid CouponCacheCenterPageRequest request);

    /**
     * 通过商品列表，查询相关优惠券
     * @param request 商品列表参数 {@link CouponCacheListForGoodsListRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cache/list-for-goods-list")
    BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsList(@RequestBody @Valid CouponCacheListForGoodsListRequest request);


    /**
     * 通过商品列表，查询相关优惠券
     * @param request 商品列表参数 {@link CouponCacheListForGoodsListRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cache/list-for-goods-list-goodinfolistrequest")
    BaseResponse<CouponCacheListForGoodsDetailResponse> listCouponForGoodsList(@RequestBody @Valid CouponCacheListForGoodsGoodInfoListRequest request);

    /**
     * 列表请求参数
     * @param request 商品信息id {@link CouponCacheListForGoodsDetailRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cache/list-for-goods-detail")
    BaseResponse<CouponCacheListForGoodsListResponse> listCouponForGoodsDetail(@RequestBody @Valid CouponCacheListForGoodsDetailRequest request);

    /**
     * 凑单页方法
     * @param request 封装参数 {@link CouponGoodsListRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/list-goods-by-coupon-id")
    BaseResponse<CouponGoodsListResponse> listGoodsByCouponId(@RequestBody @Valid CouponGoodsListRequest request);

    /**
     * 查询生效中和未生效的优惠券
     * @param request 分页请求参数 {@link CouponCacheCenterPageRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/page")
    BaseResponse<CouponCacheCenterPageResponse> pageCoupon(@RequestBody @Valid CouponCacheCenterPageRequest request);

}
