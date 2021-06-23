package com.wanmi.sbc.account.finance.record.repository;

import com.wanmi.sbc.account.finance.record.model.entity.PayItemRecord;
import com.wanmi.sbc.account.finance.record.model.entity.PaySummarize;
import com.wanmi.sbc.account.finance.record.model.entity.Reconciliation;
import com.wanmi.sbc.account.finance.record.model.entity.TotalRecord;
import com.wanmi.sbc.account.bean.enums.PayWay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>对账单持久化Bean</p>
 * Created by of628-wenzhi on 2017-12-05-下午4:49.
 */
@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, String>, JpaSpecificationExecutor<Reconciliation> {
    @Query(value = "SELECT new com.wanmi.sbc.account.finance.record.model.entity.TotalRecord(" +
            "r.supplierId,r.storeId," +
            "SUM(r.amount) AS totalAmount) " +
            "FROM Reconciliation r " +
            "WHERE " +
            "(:supplierId IS NULL OR r.supplierId = :supplierId) " +
            "AND (:storeIdsSize = 0 OR r.storeId in (:storeIds)) " +
            "AND (:typeFlag IS NULL OR r.type = :typeFlag) " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "GROUP BY r.storeId " +
            "ORDER BY totalAmount DESC",
            countQuery = "SELECT COUNT(distinct storeId) FROM Reconciliation " +
                    "WHERE storeId IN " +
                    "(" +
                    "SELECT DISTINCT storeId FROM Reconciliation r " +
                    "WHERE " +
                    "(:supplierId IS NULL OR r.supplierId = :supplierId) " +
                    "AND (:storeIdsSize = 0 OR r.storeId in (:storeIds)) " +
                    "AND (:typeFlag IS NULL OR r.type = :typeFlag) " +
                    "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
                    "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
                    "GROUP BY r.storeId" +
                    ")"
    )
    Page<TotalRecord> queryTotalRecord(@Param("beginTime") LocalDateTime beginTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("supplierId") Long supplierId,
                                       @Param("storeIdsSize") Integer storeIdsSize,
                                       @Param("storeIds") List<Long> storeIds,
                                       @Param("typeFlag") Byte typeFlag,
                                       Pageable pageable);


    @Query(value = "SELECT new com.wanmi.sbc.account.finance.record.model.entity.PayItemRecord(" +
            "r.storeId,r.payWay,SUM(r.amount) AS amount) " +
            "FROM Reconciliation r " +
            "WHERE r.storeId in (:storeIds) " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "AND r.type = :typeFlag " +
            "GROUP BY r.storeId,r.payWay"
    )
    List<PayItemRecord> queryPayItemRecord(@Param("beginTime") LocalDateTime beginTime,
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("storeIds") List<Long> storeIds,
                                           @Param("typeFlag") Byte typeFlag
    );

    @Query(value = "FROM Reconciliation r WHERE r.storeId = :storeId AND r.type = :typeFlag " +
            "AND (:payWay is NULL OR r.payWay = :payWay) " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "AND (:tradeNo = '' OR r.tradeNo like concat('%', :tradeNo, '%') ) " +
            "ORDER BY r.orderTime DESC")
    Page<Reconciliation> queryDetails(@Param("storeId") Long storeId,
                                      @Param("beginTime") LocalDateTime beginTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("typeFlag") Byte typeFlag,
                                      @Param("payWay") PayWay payWay,
                                      @Param("tradeNo") String tradeNo,
                                      Pageable pageable
    );

    @Query(value = "FROM Reconciliation r WHERE r.storeId = :storeId AND r.type = :typeFlag " +
            "AND (:payWay is NULL OR r.payWay = :payWay) " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "AND (:tradeNo = '' OR r.tradeNo like concat('%', :tradeNo, '%') ) " +
            "ORDER BY r.orderTime DESC")
    List<Reconciliation> queryDetails(@Param("storeId") Long storeId,
                                      @Param("beginTime") LocalDateTime beginTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("typeFlag") Byte typeFlag,
                                      @Param("payWay") PayWay payWay,
                                      @Param("tradeNo") String tradeNo
    );

    @Query(value = "SELECT new com.wanmi.sbc.account.finance.record.model.entity.PaySummarize(r.payWay,SUM(r.amount) AS " +
            "sumAmount)" +
            "FROM Reconciliation r " +
            "WHERE (:supplierId IS NULL OR r.supplierId = :supplierId) " +
            "AND r.type = :typeFlag " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "GROUP BY r.payWay"
    )
    List<PaySummarize> summarizing(@Param("beginTime") LocalDateTime beginTime,
                                   @Param("endTime") LocalDateTime endTime,
                                   @Param("supplierId") Long supplierId,
                                   @Param("typeFlag") Byte typeFlag
    );


    int deleteByOrderCodeAndType(String orderCode, Byte typeFlag);

    int deleteByReturnOrderCodeAndType(String returnOrderCode, Byte typeFlag);

    /**
     * 导出需要记录总数
     *
     * @param beginTime
     * @param endTime
     * @param supplierId
     * @param storeIdsSize
     * @param storeIds
     * @param typeFlag
     * @return
     */
    @Query(value = "SELECT COUNT(distinct storeId) FROM Reconciliation " +
            "WHERE storeId IN " +
            "(" +
            "SELECT DISTINCT storeId FROM Reconciliation r " +
            "WHERE " +
            "(:supplierId IS NULL OR r.supplierId = :supplierId) " +
            "AND (:storeIdsSize = 0 OR r.storeId in (:storeIds)) " +
            "AND (:typeFlag IS NULL OR r.type = :typeFlag) " +
            "AND (:beginTime is NULL OR r.tradeTime >= :beginTime ) " +
            "AND (:endTime is NULL OR r.tradeTime <= :endTime ) " +
            "GROUP BY r.storeId" +
            ")"
    )
    Integer queryCount(@Param("beginTime") LocalDateTime beginTime,
                       @Param("endTime") LocalDateTime endTime,
                       @Param("supplierId") Long supplierId,
                       @Param("storeIdsSize") Integer storeIdsSize,
                       @Param("storeIds") List<Long> storeIds,
                       @Param("typeFlag") Byte typeFlag);
}
