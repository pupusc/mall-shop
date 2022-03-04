package com.wanmi.sbc.marketing.common.repository;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 营销规则
 */
@Repository
public interface MarketingRepository extends JpaRepository<Marketing, Long>, JpaSpecificationExecutor<Marketing> {

    /**
     * 获取类型重复的skuIds
     * @param skuIds
     * @param
     * @return
     */
    @Query("select s.scopeId from Marketing m left join m.marketingScopeList s " +
            "where m.delFlag = 0 and m.marketingType = :marketingType and m.storeId = :storeId and s.scopeId in :skuIds " +
            "and not(m.beginTime > :endTime or m.endTime < :startTime) and (:excludeId is null or m.marketingId <> :excludeId)")
    List<String> getExistsSkuByMarketingType(@Param("skuIds") List<String> skuIds, @Param("marketingType") MarketingType marketingType
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId
            , @Param("excludeId") Long excludeId);


    /**
     * 容器临时处理，后续要更改 todo
     * @param skuIds
     * @param marketingType
     * @param startTime
     * @param endTime
     * @param storeId
     * @param excludeId
     * @return
     */
    @Query("select s.scopeId from Marketing m left join m.marketingScopeList s " +
            "where m.delFlag = 0 and m.marketingType = :marketingType and m.storeId = :storeId and s.scopeId in :skuIds " +
            "and not(m.beginTime > :endTime or m.endTime < :startTime)")
    List<String> getExistsSkuByMarketingTypeRongQi(@Param("skuIds") List<String> skuIds, @Param("marketingType") MarketingType marketingType
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId);

    @Query("select s.scopeId from Marketing m left join m.marketingScopeList s " +
            "where m.delFlag = 0 and m.marketingType = :marketingType and m.storeId = :storeId and s.scopeId in :skuIds " +
            "and not(m.beginTime > :endTime or m.endTime < :startTime) and m.marketingId <> :excludeId")
    List<String> getExistsSkuByMarketingTypeRongQi(@Param("skuIds") List<String> skuIds, @Param("marketingType") MarketingType marketingType
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId
            , @Param("excludeId") Long excludeId);


    /**
     * 获取重复的换购skuIds
     * @param
     * @param
     * @return
     */
//    @Query(value = "select s.goods_info_id from marketing m left join markup_level_detail s on m.marketing_id=s.markup_id " +
//            "where m.del_flag = 0 and m.marketing_type = :marketingType and m.store_id = :storeId and s.goods_info_id in :markupSkuIds " +
//            "and not(m.begin_time > :endTime or m.end_time < :startTime) and (:excludeId is null or m.marketing_id <> :excludeId)",nativeQuery = true)
//    List<String> getExistsMarkupSkuBy(@Param("markupSkuIds") List<String> markupSkuIds, @Param("marketingType") int marketingType
//            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId
//            , @Param("excludeId") Long excludeId);


    @Query(value = "select s.goods_info_id from marketing m left join markup_level_detail s on m.marketing_id=s.markup_id " +
            "where m.del_flag = 0 and m.marketing_type = :marketingType and m.store_id = :storeId and s.goods_info_id in :markupSkuIds " +
            "and not(m.begin_time > :endTime or m.end_time < :startTime) ",nativeQuery = true)
    List<String> getExistsMarkupSkuByRongQi(@Param("markupSkuIds") List<String> markupSkuIds, @Param("marketingType") int marketingType
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId);

    @Query(value = "select s.goods_info_id from marketing m left join markup_level_detail s on m.marketing_id=s.markup_id " +
            "where m.del_flag = 0 and m.marketing_type = :marketingType and m.store_id = :storeId and s.goods_info_id in :markupSkuIds " +
            "and not(m.begin_time > :endTime or m.end_time < :startTime) and m.marketing_id <> :excludeId",nativeQuery = true)
    List<String> getExistsMarkupSkuByRongQi(@Param("markupSkuIds") List<String> markupSkuIds, @Param("marketingType") int marketingType
            , @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId
            , @Param("excludeId") Long excludeId);


    /**
     * 删除活动
     * @param marketingId
     * @return
     */
    @Modifying
    @Query("update Marketing set delFlag = 1 where marketingId = :marketingId")
    int deleteMarketing(@Param("marketingId") Long marketingId);


    /**
     * 暂停活动
     * @param marketingId
     * @return
     */
    @Modifying
    @Query("update Marketing set isPause = :isPause where marketingId = :marketingId")
    int pauseOrStartMarketing(@Param("marketingId") Long marketingId, @Param("isPause") BoolFlag isPause);

    /**
     * 获取验证进行中的营销
     * @param marketingIds
     * @return
     */
    @Query("select t.marketingId from Marketing t " +
            "where t.delFlag = 0 AND now() >= t.beginTime AND now() <= t.endTime AND t.isPause = 0 AND t.marketingId in (:marketingIds)")
    List<String> queryStartingMarketing(@Param("marketingIds") List<Long> marketingIds);

    /**
     * 根据商铺Id和促销类型获取促销集合（配合全部商品选项，但是目前不考虑全部商品选项）
     * @param storeId
     * @param scopeType
     * @return
     */
    List<Marketing> findByStoreIdAndScopeType(Long storeId, MarketingScopeType scopeType);

    /**
     * 根据skuId获取进行中组合购活动的id
     * @param skuId
     * @param
     * @return
     */
    @Query(value = "select distinct m.marketing_id from marketing m left join marketing_suits_sku s on m.marketing_id = s.marketing_id " +
            "where m.del_flag = 0 and m.store_id = :storeId and m.marketing_type = 6 and s.sku_id = :skuId " +
            "and not(m.begin_time > :endTime or m.end_time < :startTime) and (:excludeId is null or m.marketing_id <> :excludeId)", nativeQuery = true)
    List<String> getMarketingSuitsExists(@Param("skuId") String skuId, @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime, @Param("storeId") Long storeId, @Param("excludeId") Long excludeId);

    /**
     * 根据skuId查询正在进行中的组合购活动
     * @param skuId
     * @return
     */
    @Query(value = "SELECT * from marketing m left join marketing_suits_sku s on m.marketing_id = s.marketing_id " +
            "where m.del_flag = 0 and m.is_pause = 0 and m.marketing_type = 6 and m.begin_time <= now() and m.end_time >= now() " +
            "and s.sku_id = :skuId", nativeQuery = true)
    List<Marketing> getMarketingBySuitsSkuId(@Param("skuId")String skuId);

    /**
     * 根据skuId查询组合购活动（排除已结束）
     * @param skuId
     * @return
     */
    @Query(value = "SELECT * from marketing m left join marketing_suits_sku s on m.marketing_id = s.marketing_id " +
            "where m.del_flag = 0 and m.is_pause = 0 and m.marketing_type = 6 and m.end_time > now() " +
            "and s.sku_id = :skuId", nativeQuery = true)
    List<Marketing> getMarketingNotEndBySuitsSkuId(@Param("skuId")String skuId);

    @Override
    @EntityGraph(value = "Marketing.Graph",type = EntityGraph.EntityGraphType.FETCH)
    Page<Marketing> findAll(Specification<Marketing> var1, Pageable var2);

}
