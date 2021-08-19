package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateGetByCouponCateIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateListLimitThreeByCateIdsRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateGetByCouponCateIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListForSupplierResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListLimitThreeByCateIdsResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>优惠券分类查询接口</p>
 * author: sunkun
 * Date: 2018-11-23
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCateQueryProvider")
public interface CouponCateQueryProvider {

    /**
     * 查询优惠券分类列表提供给优惠券使用, 最多可以批量查询3个
     * @param request 请求结构 {@link CouponCateListLimitThreeByCateIdsRequest}
     * @return {@link CouponCateListLimitThreeByCateIdsResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/list-limit-three-by-cate-ids")
    BaseResponse<CouponCateListLimitThreeByCateIdsResponse> listLimitThreeByCateIds(@RequestBody @Valid CouponCateListLimitThreeByCateIdsRequest request);

    /**
     * 查询优惠券分类列表
     * @return {@link CouponCateListResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/list")
    BaseResponse<CouponCateListResponse> list();

    /**
     * 根据优惠券分类Id查询单个优惠券分类
     * @param request 请求结构 {@link CouponCateGetByCouponCateIdRequest}
     * @return {@link CouponCateGetByCouponCateIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/get-by-coupon-cate-id")
    BaseResponse<CouponCateGetByCouponCateIdResponse> getByCouponCateId(@RequestBody @Valid CouponCateGetByCouponCateIdRequest request);

    /**
     * 查询优惠券分类列表提供给商家使用
     * @return {@link CouponCateListForSupplierResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/list-for-supplier")
    BaseResponse<CouponCateListForSupplierResponse> listForSupplier();

}
