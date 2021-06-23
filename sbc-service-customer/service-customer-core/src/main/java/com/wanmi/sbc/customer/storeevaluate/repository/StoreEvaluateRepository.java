package com.wanmi.sbc.customer.storeevaluate.repository;

import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>店铺评价DAO</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Repository
public interface StoreEvaluateRepository extends JpaRepository<StoreEvaluate, String>,
        JpaSpecificationExecutor<StoreEvaluate> {

    @Query("SELECT s.storeId,s.storeName,count(1) as orderNum from StoreEvaluate s where s.createTime >= ?1 group by s.storeId")
    List<Object> queryStoreInfoByCreatetime(@Param("stratTime") LocalDateTime stratTime);
	
}
