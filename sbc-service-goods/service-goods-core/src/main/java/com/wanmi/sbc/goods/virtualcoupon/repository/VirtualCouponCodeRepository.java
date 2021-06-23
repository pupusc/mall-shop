package com.wanmi.sbc.goods.virtualcoupon.repository;

import com.wanmi.sbc.goods.virtualcoupon.model.request.LinkOrderRequest;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCouponCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * <p>券码DAO</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@Repository
public interface VirtualCouponCodeRepository extends JpaRepository<VirtualCouponCode, Long>,
        JpaSpecificationExecutor<VirtualCouponCode> {

    /**
     * 单个删除券码
     *
     * @author 梁善
     */
    @Modifying
    @Query("update VirtualCouponCode set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除券码
     *
     * @author 梁善
     */
    @Modifying
    @Query(value = "update virtual_coupon_code set del_flag = 1,update_time=now(),update_person=:updatePerson where coupon_id=:couponId and id in (:idList)", nativeQuery = true)
    void deleteByIdList(List<Long> idList, Long couponId, String updatePerson);

    Optional<VirtualCouponCode> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    Optional<VirtualCouponCode> findByCouponIdAndIdAndDelFlag(Long couponId, Long id, DeleteFlag deleteFlag);

    /**
     * 删除券码
     *
     * @param couponId
     * @param id
     * @param updatePerson
     */
    @Modifying
    @Query(value = "update virtual_coupon_code set del_flag=1 , update_time=now() ,update_person=:updatePerson where coupon_id=:couponId and id=:id", nativeQuery = true)
    void deleteCouponCode(Long couponId, Long id, String updatePerson);

    /**
     * 获取所有券码 不根据卡券ID
     *
     * @return
     */
    @Query("select couponNo from VirtualCouponCode where delFlag=0 ")
    List<String> findAllCouponNo(Long couponId);

    /**
     * 获取失效券码数量
     *
     * @param couponId
     * @return
     */
    @Query(value = "select count(1) from virtual_coupon_code where del_flag=0 and status=2 and coupon_id=:couponId", nativeQuery = true)
    Integer findExpireStock(Long couponId);

    /**
     * 获取可用券码数量
     *
     * @param couponId
     * @return
     */
    @Query(value = "select count(1) from virtual_coupon_code where del_flag=0 and status=0 and coupon_id=:couponId", nativeQuery = true)
    Integer findAbleStock(@Param("couponId") Long couponId);

    /**
     * 获取 状态为未发放且过期的卡券
     *
     * @param couponId
     * @param now
     * @return
     */
    @Query(value = "from VirtualCouponCode where couponId=:couponId and status=0 and delFlag=0 and receiveEndTime<=:now")
    List<VirtualCouponCode> findExpireCouponCode(Long couponId, LocalDateTime now);

    @Modifying
    @Query(value = "update VirtualCouponCode set status=2,updateTime=:now,updatePerson='system' where couponId=:couponId and id in (:ids)")
    void expireCouponCode(Long couponId, List<Long> ids, LocalDateTime now);

    @Modifying
    @Query(value = "update VirtualCouponCode set status=1,tid=:#{#req.tid},updateTime=:#{#req.now},updatePerson=:#{#req.updatePerson} where couponId=:#{#req.couponId} and id in (:#{#req.ids})")
    void linkOrder(@Param("req") LinkOrderRequest req);
}
