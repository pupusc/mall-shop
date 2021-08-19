package com.wanmi.sbc.marketing.coupon.repository;

import com.wanmi.sbc.marketing.coupon.model.root.CouponCodeCopy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 优惠券码数据源 - 拷贝表，主要用来做历史数据迁移
 */
@Repository
public interface CouponCodeCopyRepository extends JpaRepository<CouponCodeCopy, String>, JpaSpecificationExecutor<CouponCodeCopy> {

    /**
     * 分页查询券码信息列表
     * @param pageable
     * @return
     */
    @Query(" from CouponCodeCopy c ")
    List<CouponCodeCopy> pageCouponCode(Pageable pageable);

    /**
     * 根据ID集合批量删除券码信息
     * @param couponCodeIds
     * @return
     */
    @Modifying
    @Query(" delete from CouponCodeCopy c where c.couponCodeId in :couponCodeIds")
    Integer deleteByCouponCodeIdsIn(@Param("couponCodeIds") List<String> couponCodeIds);
}
