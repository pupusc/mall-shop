package com.wanmi.sbc.goods.presellsale.repository;

import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface PresellSaleRepository extends JpaRepository<PresellSale, String>, JpaSpecificationExecutor<PresellSale> {

    /**
     * 通过预售活动商品id查询活动
     *
     * @return
     */
    @Query("from PresellSale c where c.id =?1 and c.delFlag = 0")
    PresellSale findPresellSaleByAndDelFlag(String id);

    /**
     * @return 查询出所有的类型为定金的预售活动
     */
    @Query("from PresellSale  where presellType =1 and delFlag =0")
    List<PresellSale> findAllByType();
}
