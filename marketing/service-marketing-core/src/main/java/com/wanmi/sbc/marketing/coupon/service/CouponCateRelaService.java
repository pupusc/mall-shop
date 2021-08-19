package com.wanmi.sbc.marketing.coupon.service;

import com.wanmi.sbc.marketing.coupon.model.root.CouponCateRela;
import com.wanmi.sbc.marketing.coupon.repository.CouponCateRelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponCateRelaService {

    @Autowired
    private CouponCateRelaRepository couponCateRelaRepository;

    /**
     * 根据优惠券ID集合查询关联分类信息
     * @param couponIdList
     * @return
     */
    public List<CouponCateRela> findByCouponIdIn(List<String> couponIdList){
       return couponCateRelaRepository.findByCouponIdIn(couponIdList);
    }
}
