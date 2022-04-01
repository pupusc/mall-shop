package com.soybean.mall.order.prize.repository;


import com.soybean.mall.order.prize.model.root.OrderCouponRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface OrderCouponRecordRepository extends JpaRepository<OrderCouponRecord, Long>,
        JpaSpecificationExecutor<OrderCouponRecord> {

    @Modifying
    @Transactional
    @Query("update OrderCouponRecord w set w.status = 1, w.updateTime = now() where w.id in ?1")
    int updateStatus(List<Long> ids);
}