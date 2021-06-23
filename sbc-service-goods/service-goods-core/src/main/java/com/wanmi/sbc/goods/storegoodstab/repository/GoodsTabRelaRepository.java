package com.wanmi.sbc.goods.storegoodstab.repository;

import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRela;
import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRelaMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>商品详情模板关联</p>
 * author: sunkun
 * Date: 2018-10-16
 */
@Repository
public interface GoodsTabRelaRepository extends JpaRepository<GoodsTabRela, GoodsTabRelaMultiKeys> {

    /**
     * 获取商品详情模板
     *
     * @return
     */
    @Query("from GoodsTabRela  where goodsId = :goodsId and tabId in (:tabIds)")
    List<GoodsTabRela> queryListByTabIds(@Param("goodsId") String goodsId, @Param("tabIds") List<Long> tabIds);

    /**
     * 根据商品Id查询商品详情模板
     * @param goodsId
     * @return
     */
    List<GoodsTabRela> findByGoodsId(String goodsId);

}
