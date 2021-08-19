package com.wanmi.sbc.order.growthvalue.repository;

import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>会员权益处理订单成长值 临时表DAO</p>
 */
@Repository
public interface OrderGrowthValueTempRepository extends JpaRepository<OrderGrowthValueTemp, Long>,
        JpaSpecificationExecutor<OrderGrowthValueTemp> {
	
}
