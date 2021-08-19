package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 优惠券商品作用范围
 */
@Repository
public interface CouponMarketingScopeRepository extends JpaRepository<CouponMarketingScope, String>, JpaSpecificationExecutor<CouponMarketingScope> {


    /**
     * 删除优惠券id相关的商品作用范围
     */
    int deleteByCouponId(String couponId);

    /**
     * 根据优惠券id获取商品作用范围
     * @param couponId 优惠券id
     * @return
     */
    List<CouponMarketingScope> findByCouponId(String couponId);

    /**
     * 按照优惠券id集合查询作用范围
     * @param couponIds
     * @return
     */
    List<CouponMarketingScope> findByCouponIdIn(List<String> couponIds);

    /**
     * 按照优惠券范围id查询作用范围的优惠券
     * @param scopeId
     * @return
     */
    List<CouponMarketingScope> findByScopeId(String scopeId);
}
