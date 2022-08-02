package com.wanmi.sbc.order.paycallbackresult.repository;

import com.wanmi.sbc.order.bean.enums.PayCallBackResultStatus;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>支付回调结果DAO</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@Repository
public interface PayCallBackResultRepository extends JpaRepository<PayCallBackResult, Long>,
        JpaSpecificationExecutor<PayCallBackResult> {

    @Modifying
    @Query("update PayCallBackResult set resultStatus = ?2,errorNum = errorNum+1 where businessId = ?1 and errorNum < 6")
    int updateStatusFailedByBusinessId(String businessId, PayCallBackResultStatus resultStatus);

    @Modifying
    @Query("update PayCallBackResult set resultStatus = ?2 where businessId = ?1")
    int updateStatusSuccessByBusinessId(String businessId, PayCallBackResultStatus resultStatus);



    @Query(value = "select * from pay_call_back_result where id > ?1 order by id asc limit ?2", nativeQuery = true)
    List<PayCallBackResult> listByMaxId(int from, int size);

    @Query(value = "select * from pay_call_back_result where business_id  ?1 and result_status = ?2", nativeQuery = true)
    List<PayCallBackResult> list(String businessId, Integer resultStatus);
}
