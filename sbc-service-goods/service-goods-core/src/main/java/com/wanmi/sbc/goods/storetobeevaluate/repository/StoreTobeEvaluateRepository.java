package com.wanmi.sbc.goods.storetobeevaluate.repository;

import com.wanmi.sbc.goods.storetobeevaluate.model.root.StoreTobeEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * <p>店铺服务待评价DAO</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@Repository
public interface StoreTobeEvaluateRepository extends JpaRepository<StoreTobeEvaluate, String>,
        JpaSpecificationExecutor<StoreTobeEvaluate> {

    @Modifying
    int deleteByOrderNoAndStoreId(String oId, long storeId);
	
}
