package com.wanmi.sbc.pay.repository;

import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.model.root.PayTradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by of628-wenzhi on 2017-08-11-下午3:41.
 */
@Repository
public interface TradeRecordRepository extends JpaRepository<PayTradeRecord, String>, JpaSpecificationExecutor<PayTradeRecord> {

    PayTradeRecord findByBusinessId(String businessId);

    @Query(value = "select * from pay_trade_record where business_id = ?1", nativeQuery = true)
    List<PayTradeRecord> listPayTradeRecord(String businessId);

    PayTradeRecord findTopByBusinessIdAndStatus(String businessId, TradeStatus status);

    long countByBusinessId(String businessId);

    PayTradeRecord findByChargeId(String chargeId);

    @Modifying
    @Query("update PayTradeRecord p set " +
            "p.status = :status, " +
            "p.practicalPrice = :price," +
            "p.finishTime= :finishTime" +
            " where id = :id")
    int updateTradeStatusAndPracticalPriceAndFinishTime(@Param("id") String id,
                                                        @Param("status") TradeStatus status,
                                                        @Param("price") BigDecimal price,
                                                        @Param("finishTime") LocalDateTime finishTime);

    int deleteByBusinessId(String businessId);

    @Modifying
    @Query("update PayTradeRecord p set " +
            "p.appId = :appId " +
            " where p.tradeNo = :tradeNo")
    int updateAppIdByTradeNo(@Param("appId")String appId, @Param("tradeNo") String tradeNo);
}
