package com.wanmi.sbc.order.receivables.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.receivables.model.root.Receivable;
import com.wanmi.sbc.order.receivables.request.ReceivableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收款单数据源操作
 * Created by zhangjin on 2017/3/20.
 */
@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, Long> {

    /**
     * 查询
     * @param receivableRequest receivableRequest
     * @param pageable pageable
     * @return 收款单
     */
    @Query("from Receivable r where r.delFlag = 0 ")
    Page<Receivable> findReceivables(ReceivableRequest receivableRequest, Pageable pageable);

    /**
     * 根据支付单号删除流水
     * @param payOrderIds payOrderIds
     * @return rows
     */
    @Query("update Receivable r set r.delFlag = 1,r.payOrderId = null, r.delTime = now() where r.payOrderId in :payOrderIds")
    @Modifying
    int deleteReceivables(@Param("payOrderIds") List<String> payOrderIds);

    /**
     * 查询
     * @param deleteFlag
     * @param payOrderId
     * @return
     */
    List<Receivable> findByDelFlagAndPayOrderId(DeleteFlag deleteFlag, String payOrderId);
}
