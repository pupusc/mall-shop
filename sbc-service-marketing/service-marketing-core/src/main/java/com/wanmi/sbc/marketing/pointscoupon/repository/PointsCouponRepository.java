package com.wanmi.sbc.marketing.pointscoupon.repository;

import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>积分兑换券表DAO</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@Repository
public interface PointsCouponRepository extends JpaRepository<PointsCoupon, Long>,
        JpaSpecificationExecutor<PointsCoupon> {

    /**
     * 单个删除积分兑换券表
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsCoupon set delFlag = 1 where pointsCouponId = ?1")
    void deleteById(Long pointsCouponId);

    /**
     * 积分兑换优惠券，扣除一个库存
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsCoupon p set p.exchangeCount = p.exchangeCount+1 where p.pointsCouponId = ?1 and p.exchangeCount < p.totalCount")
    int deductStock(Long pointsCouponId);

    /**
     * 库存为0，设为售罄
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsCoupon p set p.sellOutFlag = 1 where p.pointsCouponId = ?1 and p.exchangeCount = p.totalCount")
    int updateSellOutFlag(Long pointsCouponId);

    /**
     * 查询过期积分兑换券
     *
     * @return
     */
    @Query("select c from PointsCoupon c where c.endTime< now() and delFlag = 0 and status = 1")
    List<PointsCoupon> queryOverdueList();
}
