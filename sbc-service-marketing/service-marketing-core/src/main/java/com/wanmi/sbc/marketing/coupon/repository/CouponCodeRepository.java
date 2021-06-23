package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券码数据源
 */
@Repository
public interface CouponCodeRepository extends JpaRepository<CouponCode, String>, JpaSpecificationExecutor<CouponCode> {

    /**
     * 查询该用户在活动+优惠券条件下，是否有未使用的券，保证不能重复领取
     *
     * @param customerId
     * @param couponId
     * @param activityId
     * @return
     */
    @Query("select count(couponCodeId) from CouponCode where customerId = ?1 and couponId = ?2 and activityId = ?3 " +
            "and useStatus = 0 and delFlag = 0 and startTime > now() and endTime < now()")
    int countCustomerHasCoupon(String customerId, String couponId, String activityId);

    /**
     * 查询该用户在活动+优惠券条件下，已经领取的次数
     *
     * @param customerId
     * @param couponId
     * @param activityId
     * @return
     */
    @Query("select count(couponCodeId) from CouponCode where customerId = ?1 and couponId = ?2 and activityId = ?3 ")
    int countCustomerHasFetchedCoupon(String customerId, String couponId, String activityId);

    /**
     * 查询优惠券已经使用多少
     *
     * @param couponId
     * @param activityId
     * @return
     */
    @Query("select count(couponCodeId) from CouponCode where couponId = ?1 and activityId = ?2")
    int countCouponUsed(String couponId, String activityId);

    /**
     * 查询用户已领的券
     *
     * @param customerId
     * @return
     */
    @Query("from CouponCode where customerId = ?1 and useStatus = 0 and delFlag = 0")
    List<CouponCode> findByCustomerId(String customerId);

    /**
     * 根据id列表批量查询
     *
     * @param ids
     * @return
     */
    @Query("from CouponCode where couponCodeId in ?1")
    List<CouponCode> findByIds(List<String> ids);

    /**
     * 根据CouponCodeId查询优惠券码
     *
     * @param couponCodeId
     * @return
     */
    @Query(" from CouponCode c where c.couponCodeId = ?1 ")
    CouponCode findByCouponCodeId(String couponCodeId);

    /**
     * 统计用户的券数量
     */
    Integer countByCustomerId(String customerId);

    /**
     * 根据customerId和activityId查询优惠券数量
     */
    Integer countByCustomerIdAndActivityId(String customerId, String activityId);

    @Modifying
    @Query("update CouponCode c set c.useStatus = ?3 , c.orderCode = ?4 , c.updateTime = now() where c.couponCodeId = ?1 and c.customerId = ?2 ")
    Integer updateCouponCode(String couponCodeId, String customerId, DefaultFlag useStatus,String orderCode);

    @Modifying
    @Query("update CouponCode c set c.useStatus = 0 , c.orderCode = null, c.useDate = null, c.updateTime = now() where c.couponCodeId = ?1 and c.customerId = ?2 ")
    Integer returnCoupon(String couponCodeId, String customerId);
}
