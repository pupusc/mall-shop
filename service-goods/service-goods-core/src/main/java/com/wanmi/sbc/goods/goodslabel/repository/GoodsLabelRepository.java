package com.wanmi.sbc.goods.goodslabel.repository;

import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>商品标签DAO</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@Repository
public interface GoodsLabelRepository extends JpaRepository<GoodsLabel, Long>,
        JpaSpecificationExecutor<GoodsLabel> {

    /**
     * 单个删除商品标签
     */
    @Modifying
    @Query("update GoodsLabel set delFlag = 1 where goodsLabelId = ?1 and storeId = ?2")
    void deleteById(Long goodsLabelId, Long storeId);

    /**
     * 批量删除商品标签
     */
    @Modifying
    @Query("update GoodsLabel set delFlag = 1 where goodsLabelId in ?1 and storeId = ?2")
    int deleteByIdList(List<Long> goodsLabelIdList, Long storeId);
}
