package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 会员等级权益发放优惠券
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCustomerRightsProvider")
public interface CouponCustomerRightsProvider {

    /**
     * 会员等级权益发放优惠券
     *
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/customer-rights/issue-coupons")
    BaseResponse customerRightsIssueCoupons();
}
