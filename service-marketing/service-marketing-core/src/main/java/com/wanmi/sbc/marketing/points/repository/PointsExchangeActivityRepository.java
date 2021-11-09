package com.wanmi.sbc.marketing.points.repository;


import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivity;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsExchangeActivityRepository extends JpaRepository<PointsExchangeActivity, Integer>,
        JpaSpecificationExecutor<PointsExchangeActivity> {
}
