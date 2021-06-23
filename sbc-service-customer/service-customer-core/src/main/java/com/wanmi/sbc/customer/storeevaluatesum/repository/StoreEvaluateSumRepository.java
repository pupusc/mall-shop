package com.wanmi.sbc.customer.storeevaluatesum.repository;

import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <p>店铺评价DAO</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Repository
public interface StoreEvaluateSumRepository extends JpaRepository<StoreEvaluateSum, Long>,
        JpaSpecificationExecutor<StoreEvaluateSum> {
	
}
