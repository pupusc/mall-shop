package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsPriceSync;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface GoodsPriceSyncRepository extends JpaRepository<GoodsPriceSync, String>, JpaSpecificationExecutor<GoodsPriceSync> {

    //@Query
    List<GoodsPriceSync> findByStatus(Integer status);


    @Modifying
    @Query("update GoodsPriceSync set status = 1,updateTime = now() where id = ?1")
    int updateStatus(Long id);

    @Transactional
    @Modifying
    @Query("update GoodsPriceSync set status = 1,updateTime = now() where id in ?1")
    int updateStatusByIds(List<Long> ids);



}

