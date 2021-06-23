package com.wanmi.sbc.order.yzorder.repository;

import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.yzorder.model.root.YzOrderRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YzOrderRecordRepository extends JpaRepository<YzOrderRecord, String>, JpaSpecificationExecutor<YzOrderRecord> {

    @Query("select tid from YzOrderRecord")
    List<String> findTidBy(Pageable pageable);


    @Modifying
    @Query("update YzOrderRecord set flag = ?1 where tid in ?2")
    int updateFlag(Boolean flag, List<String> tids);
}
