package com.soybean.mall.order.prize.repository;


import com.soybean.mall.order.prize.model.root.OrderCouponRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderCouponRecordRepository extends JpaRepository<OrderCouponRecord, Long>,
        JpaSpecificationExecutor<OrderCouponRecord> {

}