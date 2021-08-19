package com.wanmi.sbc.customer.storeevaluatenum.repository;

import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>店铺统计评分等级人数统计DAO</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Repository
public interface StoreEvaluateNumRepository extends JpaRepository<StoreEvaluateNum, String>,
        JpaSpecificationExecutor<StoreEvaluateNum> {

    List<StoreEvaluateNum> findByStoreIdAndScoreCycle(Long StoreId,Integer scoreCycle);
	
}
