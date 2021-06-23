package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeBatchAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券商品作用范围操作接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponMarketingScopeProvider")
public interface CouponMarketingScopeProvider {

    /**
     * 批量新增优惠券商品作用范围
     *
     * @param request 批量新增优惠券商品作用范围请求结构 {@link CouponMarketingScopeBatchAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/marketing/scope/batch-add")
    BaseResponse batchAdd(@RequestBody @Valid CouponMarketingScopeBatchAddRequest request);

}