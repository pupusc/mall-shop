package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 11:39 AM 2018/9/12
 * @Description: 优惠券活动Repository
 */
@Repository
public interface CouponActivityRepository extends JpaRepository<CouponActivity, String>,
        JpaSpecificationExecutor<CouponActivity> {

    @Modifying
    @Query("update CouponActivity a set a.pauseFlag = 0 where a.activityId = ?1")
    int startActivity(String id);

    @Modifying
    @Query("update CouponActivity a set a.pauseFlag = 1 where a.pauseFlag = 0 and a.activityId = ?1")
    int pauseActivity(String id);

    @Modifying
    @Query("update CouponActivity a set a.delFlag = 1 ,a.delTime = now() ,a.delPerson = ?2 where a.activityId = ?1")
    int deleteActivity(String id, String operatorId);

    @Query("from CouponActivity where delFlag = 0 and startTime < now() and endTime > now()")
    List<CouponActivity> getLastActivity(Pageable pageable);

    /**
     * 查询指定时间内有多少活动
     * @param statTime
     * @param endTime
     * @param type
     * @param storeId
     * @return
     */
    @Query("from CouponActivity where delFlag = 0 and startTime >= ?1 and endTime <= ?2 and couponActivityType = ?3 and storeId = ?4")
    List<CouponActivity> queryActivityByTime(LocalDateTime statTime, LocalDateTime endTime, CouponActivityType type, Long storeId);

    /**
     * 通过活动类型查询正在进行中的活动,并且活动优惠券组数>0
     * @param type
     * @return
     */
    @Query("from CouponActivity where delFlag = 0 and couponActivityType = ?1 and storeId = ?2 and leftGroupNum > 0 and pauseFlag = 0 and startTime < now() and endTime > now()")
    List<CouponActivity> queryGoingActivityByType(CouponActivityType type,Long storeId);

    /**
     * 领取一组优惠券
     * @param activityId
     * @return
     */
    @Modifying
    @Query("update CouponActivity a set a.leftGroupNum = a.leftGroupNum - 1 where a.activityId = ?1 and leftGroupNum > 0 and a.delFlag = 0 and pauseFlag = 0 ")
    int getCouponGroup(String activityId);

    /**
     * 查询活动（注册赠券活动、进店赠券活动）不可用的时间范围
     * @param type
     * @param storeId
     * @return
     */
    @Query("from CouponActivity where delFlag =0  and couponActivityType = ?1 and storeId =?2 and endTime>= now() order by startTime ")
    List<CouponActivity> queryActivityDisableTime(CouponActivityType type , Long storeId);

    /**
     * 查询分销邀新赠券活动
     * @return
     */
    @Query("from CouponActivity where couponActivityType = 5")
    CouponActivity findDistributeCouponActivity();

    @Query(" select w.activityId from CouponActivity w where w.delFlag = 0")
    List<String> listByPage(Pageable pageable);
}
