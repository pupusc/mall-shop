package com.wanmi.sbc.goods.price.repository;

import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品级别价格数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsLevelPriceRepository extends JpaRepository<GoodsLevelPrice, Long> {

    /**
     * 根据商品ID查询SPU的级别价
     *
     * @param goodsId 商品ID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsId = ?1 and w.type = 0")
    List<GoodsLevelPrice> findByGoodsId(String goodsId);

    /**
     * 根据商品SkuID查询SKU的级别价
     *
     * @param goodsInfoId 商品SkuID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsInfoId = ?1 and w.type = 1")
    List<GoodsLevelPrice> findSkuByGoodsInfoId(String goodsInfoId);

    /**
     * 根据商品SkuID查询SKU的级别价
     *
     * @param goodsInfoIds 多商品SkuID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsInfoId in ?1 and w.type = ?2")
    List<GoodsLevelPrice> findSkuByGoodsInfoIds(List<String> goodsInfoIds, PriceType type);

    /**
     * 根据商品SkuID查询SKU的级别价
     *
     * @param goodsInfoIds 多商品SkuID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsInfoId in ?1 and w.type = ?2")
    List<GoodsLevelPrice> findSkuByGoodsInfoIdsAndType(List<String> goodsInfoIds, PriceType type);

    /**
     * 根据商品ID和批量查询SKU的级别价
     *
     * @param goodsInfoIds 商品ID
     * @param levelId      会员ID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsInfoId in ?1 and w.levelId = ?2 and w.type = 1")
    List<GoodsLevelPrice> findSkuByGoodsInfoIdAndLevelId(List<String> goodsInfoIds, Long levelId);

    /**
     * 根据批量商品ID和批量等级查询SKU的级别价
     *
     * @param goodsInfoIds 商品ID
     * @param levelIds     会员ID
     * @return
     */
    @Query("from GoodsLevelPrice w where w.goodsInfoId in ?1 and w.levelId in ?2 and w.type = 1")
    List<GoodsLevelPrice> findSkuByGoodsInfoIdAndLevelIds(List<String> goodsInfoIds, List<Long> levelIds);

    /**
     * 根据商品ID删除
     *
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("delete from GoodsLevelPrice w where w.goodsId = ?1 and w.type = 0")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品SkuID删除
     *
     * @param goodsInfoId 商品SkuID
     */
    @Modifying
    @Query("delete from GoodsLevelPrice w where w.goodsInfoId = ?1")
    void deleteByGoodsInfoId(String goodsInfoId);

    /**
     * 根据商品SkuID批量删除
     *
     * @param goodsInfoId 商品SkuID
     */
    @Modifying
    @Query("delete from GoodsLevelPrice w where w.goodsInfoId in ?1 and w.type=1 ")
    void deleteByGoodsInfoIds(List<String> goodsInfoId);

    /**
     * 根据商品SkuID和类型批量删除
     *
     * @param goodsInfoIds 商品SkuID
     */
    @Modifying
    @Query("delete from GoodsLevelPrice w where w.goodsInfoId in (:goodsInfoIds) and w.type = :priceType")
    void deleteByGoodsInfoIdsAndType(List<String> goodsInfoIds,PriceType priceType);


    List<GoodsLevelPrice> findByGoodsInfoIdAndType(String goodsInfoId, PriceType type);

    @Modifying
    @Query("delete from GoodsLevelPrice w where w.goodsInfoId =:goodsInfoId and w.type = :priceType")
    void deleteByGoodsInfoIdAndType(String goodsInfoId, PriceType priceType);

    @Query("from GoodsLevelPrice w where w.goodsInfoId in (:goodsInfoIds) and w.type = 2")
    List<GoodsLevelPrice> findEnterpriseByGoodsInfoIds(List<String> goodsInfoIds);
}
