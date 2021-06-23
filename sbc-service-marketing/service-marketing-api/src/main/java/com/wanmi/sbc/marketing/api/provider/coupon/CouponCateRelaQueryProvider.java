package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateRelaListByCouponIdsRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateRelaListByCouponIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>优惠券关联分类查询接口</p>
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCateRelaQueryProvider")
public interface CouponCateRelaQueryProvider {

    /**
     * 根据优惠券ID集合查询关联的分类信息 和 营销范围列表信息
     * @param request 请求结构 {@link CouponCateRelaListByCouponIdsRequest}
     * @return {@link CouponCateRelaListByCouponIdsResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/cate/rela/list-by-cate-ids-map")
    BaseResponse<CouponCateRelaListByCouponIdsResponse> listByCateIdsMap(@RequestBody @Valid CouponCateRelaListByCouponIdsRequest request);

}
