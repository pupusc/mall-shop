package com.wanmi.sbc.customer.growthvalue.repository;

import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yang
 * @since 2019/2/23
 */
@Repository
public interface CustomerGrowthValueRepository extends JpaRepository<CustomerGrowthValue, Long>,
        JpaSpecificationExecutor<CustomerGrowthValue> {

    /**
     * 根据业务类型获得积分查询
     *
     * @param serviceType
     * @return
     */
    List<CustomerGrowthValue> findByServiceType(Integer serviceType);

    @Query("select sum(growthValue) from CustomerGrowthValue where customerId = ?1 and type = ?2 and opTime >=?3 and opTime <= ?4")
    Integer getGrowthValueToday(String customerId, OperateType type, LocalDateTime startTime, LocalDateTime endTime);
}
