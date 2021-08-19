package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: songhanlin
 * @Date: Created In 11:39 AM 2018/9/12
 * @Description: 优惠券信息Repository
 */
@Repository
public interface CouponInfoRepository extends JpaRepository<CouponInfo, String>, JpaSpecificationExecutor<CouponInfo> {

//    @Modifying
    @Query("from CouponInfo w where w.delFlag = 0 and w.couponId in ?1")
    List<CouponInfo> queryByIds(List<String> ids);

    Optional<CouponInfo> findByCouponIdAndStoreIdAndDelFlag(String couponId, Long storeId, DeleteFlag deleteFlag);

    @Modifying
    @Query("update CouponInfo a set a.delFlag = 1 ,a.delTime = now() ,a.delPerson = ?2 where a.couponId = ?1")
    int deleteCoupon(String id, String operatorId);

    @Query(" select w.couponId from CouponInfo w where w.delFlag = 0")
    List<String> listByPage(Pageable pageable);

}
