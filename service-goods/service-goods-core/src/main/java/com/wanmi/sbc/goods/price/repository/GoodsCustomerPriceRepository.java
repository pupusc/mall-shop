package com.wanmi.sbc.goods.price.repository;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品客户价格数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsCustomerPriceRepository extends JpaRepository<GoodsCustomerPrice, Long> {

    /**
     * 根据商品ID查询SPU客户价
     *
     * @param goodsId 商品ID
     * @return
     */
    @Query("from GoodsCustomerPrice w where w.goodsId = ?1 and w.type = 0")
    List<GoodsCustomerPrice> findByGoodsId(String goodsId);

    /**
     * 根据商品SkuID查询SKU客户价
     *
     * @param goodsInfoId 商品SkuID
     * @return
     */
    @Query("from GoodsCustomerPrice w where w.goodsInfoId = ?1 and w.type = 1")
    List<GoodsCustomerPrice> findSkuByGoodsInfoId(String goodsInfoId);

    /**
     * 根据商品SkuID查询SKU客户价
     *
     * @param goodsInfoIds 商品SkuID
     * @return
     */
    @Query("from GoodsCustomerPrice w where w.goodsInfoId in ?1 and w.type = 1")
    List<GoodsCustomerPrice> findSkuByGoodsInfoIds(List<String> goodsInfoIds);

    /**
     * 根据商品ID和批量查询
     *
     * @param goodsInfoIds 商品SkuID
     * @param customerId   客户ID
     * @return
     */
    @Query("from GoodsCustomerPrice w where w.customerId = :customerId and w.goodsInfoId in :goodsInfoIds and w.type = 1")
    List<GoodsCustomerPrice> findSkuByGoodsInfoIdAndCustomerId(@Param("goodsInfoIds") List<String> goodsInfoIds, @Param("customerId") String customerId);

    /**
     * 根据商品ID删除
     *
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("delete from GoodsCustomerPrice w  where w.goodsId = ?1 and w.type = 0")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品SkuID删除
     *
     * @param goodsInfoId 商品ID
     */
    @Modifying
    @Query("delete from GoodsCustomerPrice w  where w.goodsInfoId = ?1")
    void deleteByGoodsInfoId(String goodsInfoId);

    /**
     * 根据商品SkuID批量删除
     *
     * @param goodsInfoId 商品ID
     */
    @Modifying
    @Query("delete from GoodsCustomerPrice w  where w.goodsInfoId in ?1 and w.type=1 ")
    void deleteByGoodsInfoIds(List<String> goodsInfoId);

    List<GoodsCustomerPrice> findByGoodsInfoIdAndType(String goodsInfoId, PriceType type);

    @Modifying
    @Query("delete from GoodsCustomerPrice w  where w.goodsInfoId in (:goodsInfoIds) and w.type=:priceType")
    void deleteByGoodsInfoIdsAndType(List<String> goodsInfoIds, PriceType priceType);

    @Modifying
    @Query("delete from GoodsCustomerPrice w  where w.goodsInfoId = :goodsInfoId and w.type=:priceType")
    void deleteByGoodsInfoIdAndType(String goodsInfoId, PriceType priceType);

    @Query("from GoodsCustomerPrice w where w.customerId = :customerId and w.goodsInfoId in :goodsInfoIds and w.type = 2")
    List<GoodsCustomerPrice> findEnterpriseByGoodsInfoIdAndCustomerId(@Param("goodsInfoIds") List<String> goodsInfoIds, @Param("customerId") String customerId);
}
