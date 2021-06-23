package com.wanmi.sbc.goods.storegoodstab.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 商品模板配置
 * Created by xiemengnan on 2018/10/12.
 */
@Repository
public interface StoreGoodsTabRepository extends JpaRepository<StoreGoodsTab, Long>,
        JpaSpecificationExecutor<StoreGoodsTab> {

    /**
     * 获取商家所绑定的模板列表
     *
     * @return
     */
    @Query("from StoreGoodsTab where delFlag = '0' and storeId = :storeId ")
    List<StoreGoodsTab> queryTabByStoreId(@Param("storeId") Long storeId);

    /**
     * 查询当前最大排序
     *
     * @return
     */
    @Query("select max(sort) from StoreGoodsTab where delFlag = '0' and storeId = :storeId ORDER BY sort desc")
    Integer queryMaxSortByStoreId(@Param("storeId") Long storeId);

    /**
     * sort排序返回集合
     *
     * @param storeId
     * @param sort
     * @return
     */
    @Query
    List<StoreGoodsTab> findAllByStoreIdAndDelFlagOrderBySortAsc(Long storeId, DeleteFlag sort);
}
