package com.wanmi.sbc.goods.goodsrestrictedsale.repository;

import com.wanmi.sbc.goods.goodsrestrictedsale.model.root.GoodsRestrictedSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>限售配置DAO</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@Repository
public interface GoodsRestrictedSaleRepository extends JpaRepository<GoodsRestrictedSale, Long>,
        JpaSpecificationExecutor<GoodsRestrictedSale> {

    /**
     * 单个删除限售配置
     * @author baijz
     */
    @Modifying
    @Override
    @Query("update GoodsRestrictedSale set delFlag = 1 where restrictedId = ?1")
    void deleteById(Long restrictedId);

    /**
     * 批量删除限售配置
     * @author baijz
     */
    @Modifying
    @Query("update GoodsRestrictedSale set delFlag = 1 where restrictedId in ?1")
    int deleteByIdList(List<Long> restrictedIdList);

    /**
     * 根据goodsInfoIds批量删除限售配置
     * @author baijz
     */
    @Modifying
    @Query("update GoodsRestrictedSale set delFlag = 1 where goodsInfoId in ?1")
    int deleteAllByGoodsInfoIds(List<String> goodsInfoIds);

}
