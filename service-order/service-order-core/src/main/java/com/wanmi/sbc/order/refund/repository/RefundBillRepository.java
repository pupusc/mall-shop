package com.wanmi.sbc.order.refund.repository;

import com.wanmi.sbc.order.refund.model.root.RefundBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 退款流水
 * Created by zhangjin on 2017/4/21.
 */
@Repository
public interface RefundBillRepository extends JpaRepository<RefundBill, String> {

    /**
     * 根据支付单id删除流水
     * @param refundId refundId
     * @return rows
     */
    @Modifying
    @Query("update RefundBill r set r.delFlag = 1, r.refundId = null where r.refundId = :refundId")
    int deleteBillByRefundId(@Param("refundId") String refundId);
}
