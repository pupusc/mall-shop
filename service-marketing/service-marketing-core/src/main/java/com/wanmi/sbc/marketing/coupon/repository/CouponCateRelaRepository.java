package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCateRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponCateRelaRepository extends JpaRepository<CouponCateRela, String>, JpaSpecificationExecutor<CouponCateRela> {

    /**
     * 删除优惠券id相关的优惠券分类
     */
    int deleteByCouponId(String couponId);

    /**
     * 根据优惠券id获取优惠券分类
     * @param couponId 优惠券id
     * @return
     */
    List<CouponCateRela> findByCouponId(String couponId);


    /**
     * 根据优惠券id集合获取优惠券分类
     * @param couponIdList 优惠券id集合
     * @return
     */
    List<CouponCateRela> findByCouponIdIn(List<String> couponIdList);

    /**
     * 根据分类id与平台标志删除
     * @param cateId
     * @param platformFlag
     * @return
     */
    int deleteByCateIdAndPlatformFlag(String cateId, DefaultFlag platformFlag);

    /**
     * 根据分类id删除
     * @param cateId
     * @return
     */
    int deleteByCateId(String cateId);

}
