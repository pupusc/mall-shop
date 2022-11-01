package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsFreightHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsFreightHistoryRepository extends JpaRepository<GoodsFreightHistory, String>, JpaSpecificationExecutor<GoodsFreightHistory> {
}
