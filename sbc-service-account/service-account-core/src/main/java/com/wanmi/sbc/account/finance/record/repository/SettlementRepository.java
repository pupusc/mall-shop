package com.wanmi.sbc.account.finance.record.repository;

import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.account.finance.record.model.response.TotalSettlement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by hht on 2017/12/7.
 */
@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long>, JpaSpecificationExecutor<Settlement> {

    void deleteByStoreIdAndStartTimeAndEndTime(Long storeId, String startTime, String endTime);

    @Modifying
    @Query("update Settlement set settleStatus = :settleStatus, settleTime = now() where settleId in (:settleIdList)")
    void updateSettleStatusBatch(@Param("settleStatus") SettleStatus settleStatus, @Param("settleIdList") List<Long>
            settleIdList);

    @Query("SELECT new com.wanmi.sbc.account.finance.record.model.response.TotalSettlement(SUM(storePrice),settleStatus," +
            "storeId ) FROM Settlement WHERE  storeId = :storeId GROUP BY settleStatus")
    List<TotalSettlement> queryToTalSettlement(@Param("storeId") Long storeId);

    @Query(value = " SELECT * FROM settlement WHERE  store_id = :storeId order by end_time desc LIMIT 1 ",
            nativeQuery = true)
    Optional<Settlement> getLastSettlementByStoreId(@Param("storeId") Long storeId);


    @Query(" select settleId  from Settlement  ")
    List<Long> listByPage(Pageable pageable);

    @Query(" select storeId  from Settlement  where settleId in (:settleIdList) ")
    List<Long> getStoreIdsByIds(@Param("settleIdList") List<Long> settleIdList);

}
