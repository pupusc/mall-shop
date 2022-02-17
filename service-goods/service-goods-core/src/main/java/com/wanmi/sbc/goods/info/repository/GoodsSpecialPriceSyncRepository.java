package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPriceSync;
import com.wanmi.sbc.goods.info.model.root.GoodsSpecialPriceSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface GoodsSpecialPriceSyncRepository extends JpaRepository<GoodsSpecialPriceSync, Integer>, JpaSpecificationExecutor<GoodsSpecialPriceSync> {

    @Query(value = "from GoodsSpecialPriceSync g where g.goodsNo in ?1 and g.deleted = 0 and g.status = 1")
    List<GoodsSpecialPriceSync> findByGoodsNo(List<String> goodsNos);
}