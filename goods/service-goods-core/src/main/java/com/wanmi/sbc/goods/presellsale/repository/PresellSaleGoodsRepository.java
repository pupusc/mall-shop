package com.wanmi.sbc.goods.presellsale.repository;

import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSaleGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PresellSaleGoodsRepository extends JpaRepository<PresellSaleGoods, String>, JpaSpecificationExecutor<PresellSaleGoods> {


    /**
     * 通过预售活动id查询关联商品信息
     *
     * @return
     */
    List<PresellSaleGoods> findAllByPresellSaleId(String presellSaleId);

    /**
     * 根据预售活动id删除所有关联商品
     *
     * @param presellSaleId
     * @return
     */
    @Modifying
    @Query("update PresellSaleGoods set delFlag = 1 where presellSaleId = ?1")
    int deletePresellSaleGoods(String presellSaleId);


    /**
     * 根据活动id和好预售活动商品id查询
     *
     * @return
     */
    PresellSaleGoods findByPresellSaleIdAndGoodsInfoId(String presellSaleId, String goodsInfoId);


    /**
     * 根据预售活动关联商品id查询关联商品信息
     * @param presellSaleGoodsId
     * @return
     */
    @Query("from PresellSaleGoods where id=?1 and delFlag=0")
    PresellSaleGoods findSaleGoods(String presellSaleGoodsId);

    /**
     * 根据预售活动id和关联商品id查询关联商品信息
     * @param presellSaleId
     * @param goodsInfoId
     * @return
     */
    @Query("from PresellSaleGoods where presellSaleId=?1 and goodsInfoId =?2 and delFlag=0")
    PresellSaleGoods findSaleGoodsByGoodsInfoId(String presellSaleId, String goodsInfoId);
}
