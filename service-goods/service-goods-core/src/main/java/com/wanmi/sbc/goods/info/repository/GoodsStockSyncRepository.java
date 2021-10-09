package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;




@Repository
public interface GoodsStockSyncRepository extends JpaRepository<GoodsStockSync, String>, JpaSpecificationExecutor<GoodsStockSync> {

    //@Query
    List<GoodsStockSync> findByStatus(Integer status);


    @Modifying
    @Query("update GoodsStockSync set status = 1,updateTime = now() where id = ?1")
    int updateStatus(Long id);

}
