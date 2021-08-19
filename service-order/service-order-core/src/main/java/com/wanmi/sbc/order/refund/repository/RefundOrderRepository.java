package com.wanmi.sbc.order.refund.repository;


import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.refund.model.root.RefundOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 退款单数据源
 * Created by zhangjin on 2017/4/21.
 */
public interface RefundOrderRepository extends JpaRepository<RefundOrder, String>, JpaSpecificationExecutor<RefundOrder> {

    /**
     * 修改退单状态
     * @param refundStatus refundStatus
     * @param refundIds refundIds
     * @return rows
     */
    @Modifying
    @Query("update RefundOrder r set r.refundStatus = :refundStatus where r.refundId in :refundIds")
    int updateRefundOrderStatus(@Param("refundStatus") RefundStatus refundStatus, @Param("refundIds") List<String>
            refundIds);

    /**
     * 修改退单备注
     * @param refundId refundId
     * @param comment comment
     * @return rows
     */
    @Modifying
    @Query("update RefundOrder r set r.refuseReason = :comment where r.refundId = :refundId")
    int updateRefundOrderReason(@Param("refundId") String refundId, @Param("comment") String comment);

    Optional<RefundOrder> findAllByReturnOrderCodeAndDelFlag(String returnOrderCode, DeleteFlag delFlag);

    Optional<RefundOrder> findByRefundIdAndDelFlag(String refundId, DeleteFlag delFlag);

    /**
     * 查询已退款的退款金额
     * @return
     */
    @Query("select sum(r.returnPrice) from RefundOrder r where r.refundStatus = 2")
    BigDecimal sumReturnPrice();
}
