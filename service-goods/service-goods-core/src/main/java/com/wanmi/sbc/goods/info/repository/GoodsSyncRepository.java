package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GoodsSyncRepository extends JpaRepository<GoodsSync, String>, JpaSpecificationExecutor<GoodsSync> {

//    @Query
    List<GoodsSync> findByStatus(Integer status);
    @Modifying
    @Query("update GoodsSync w set w.status = ?2, w.updateTime = now() where w.goodsNo = ?1")
    int updateStatus(String goodsNo,Integer status);

}



