package com.wanmi.sbc.goods.cate.repository;

import com.wanmi.sbc.goods.cate.model.root.GoodsCateSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsCateSyncRepository extends JpaRepository<GoodsCateSync, Integer>, JpaSpecificationExecutor<GoodsCateSync> {

    @Query("from GoodsCateSync where deleted = 0")
    List<GoodsCateSync> query();
}
