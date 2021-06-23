package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingCustomerScope;
import com.wanmi.sbc.marketing.coupon.repository.CouponMarketingCustomerScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>优惠券活动目标客户作用范围DAO</p>
 * @author lq
 * @date 2019-08-02 14:50:57
 */
@Service
public class CouponMarketingCustomerScopeService {

    @Autowired
    private CouponMarketingCustomerScopeRepository couponMarketingCustomerScopeRepository;

    /**
     * 根据活动id目标客户作用范围
     * @param activityId 活动id
     * @return
     */
    public List<CouponMarketingCustomerScope> findByActivityId(String activityId){
       return couponMarketingCustomerScopeRepository.findByActivityId(activityId);
    }

}
