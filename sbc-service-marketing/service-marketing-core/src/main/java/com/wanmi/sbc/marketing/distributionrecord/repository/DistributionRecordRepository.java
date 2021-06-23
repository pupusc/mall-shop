package com.wanmi.sbc.marketing.distributionrecord.repository;

import com.wanmi.sbc.marketing.distributionrecord.model.root.DistributionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>DistributionRecordDAO</p>
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Repository
public interface DistributionRecordRepository extends JpaRepository<DistributionRecord, String>,
        JpaSpecificationExecutor<DistributionRecord> {

    /**
     * 根据订单号和货品Id查询分销记录
     * @param goodsInfoId
     * @param tradeId
     * @return
     */
    DistributionRecord findDistributionRecordByGoodsInfoIdAndTradeId(@Param("goodsInfoId") String goodsInfoId, @Param
            ("tradeId") String tradeId);

    /**
     * 根据订单号查询分销记录
     * @param tradId
     * @return
     */
    List<DistributionRecord> findDistributionRecordsByTradeId(@Param("tradeId") String tradId);


    /**
     * 根据订单号更新分销记录的完成时间
     * @param tradeId
     * @return
     */
    @Modifying
    @Query("update DistributionRecord dr set dr.finishTime = :finishTime where dr.tradeId = :tradeId")
    int updateFinishTimeByTradeId(@Param("tradeId")String tradeId, @Param("finishTime") LocalDateTime finishTime);

    /**
     * 根据订单号和货品Id更新分销记录的入账时间和入账状态
     * @param tradeId
     * @return
     */
    @Modifying
    @Query("update DistributionRecord dr set dr.missionReceivedTime = :receivedTime ,dr.commissionState = '1' where " +
            "dr.tradeId = :tradeId and dr.goodsInfoId = :goodsInfoId")
    int updateCommissionReceivedTimeByTradeIdAndGoodsInfoId(@Param("tradeId")String tradeId, @Param("receivedTime")
            LocalDateTime receivedTime ,@Param("goodsInfoId") String goodsInfoId);

    /**
     * 根据订单号和货品Id更新分销记录中订单货品的数量
     * @param tradeId
     * @return
     */
    @Modifying
    @Query("update DistributionRecord dr set dr.orderGoodsCount = :orderGoodsCount where " +
            "dr.tradeId = :tradeId and dr.goodsInfoId = :goodsInfoId")
    int updateOrderGoodsCountByTradeIdAndGoodsInfoId(@Param("tradeId")String tradeId, @Param("orderGoodsCount")
            Long orderGoodsCount ,@Param("goodsInfoId") String goodsInfoId);


    /**
     * 根据订单号软删除
     * @param tradeId
     * @return
     */
    @Modifying
    @Query("update DistributionRecord dr set dr.deleteFlag = 1,dr.orderGoodsCount = 0 where dr.tradeId = :tradeId")
    int updateDeleteFlagByTradeId(@Param("tradeId")String tradeId);
}
