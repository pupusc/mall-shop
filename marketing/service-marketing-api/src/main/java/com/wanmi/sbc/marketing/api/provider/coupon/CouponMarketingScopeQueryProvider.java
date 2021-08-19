package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeByScopeIdRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeListForUseByCustomerIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponMarketingScopeByScopeIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券商品作用范围查询接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponMarketingScopeQueryProvider")
public interface CouponMarketingScopeQueryProvider {

    /**
     * 根据优惠券范围id查询优惠券商品作用范列表
     *
     * @param request 包含优惠券范围id的查询请求结构 {@link CouponMarketingScopeByScopeIdRequest}
     * @return 优惠券商品作用范列表 {@link CouponMarketingScopeByScopeIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/marketing/scope/list-by-scope-id")
    BaseResponse<CouponMarketingScopeByScopeIdResponse> listByScopeId(@RequestBody @Valid
                                                                              CouponMarketingScopeByScopeIdRequest
                                                                              request);

}