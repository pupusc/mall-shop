package com.wanmi.sbc.customer.storestatistics.repository;

import com.wanmi.sbc.customer.storestatistics.model.root.StoreConsumerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>店铺客户消费统计表DAO</p>
 * @author yang
 * @date 2019-03-13 17:55:08
 */
@Repository
public interface StoreConsumerStatisticsRepository extends JpaRepository<StoreConsumerStatistics, String>,
        JpaSpecificationExecutor<StoreConsumerStatistics> {

    /**
     * 根据客户编号 店铺编号查询
     * @param customerId
     * @param storeId
     * @return
     */
    StoreConsumerStatistics findByCustomerIdAndStoreId(String customerId, Long storeId);
	
}
