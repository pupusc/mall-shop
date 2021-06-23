package com.wanmi.sbc.order.orderinvoice.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单开票
 * Created by CHENLI on 2017/5/5.
 */
@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice,String>, JpaSpecificationExecutor<OrderInvoice> {
    /**
     * 根据订单号查订单开票信息
     * @param orderNo
     * @param deleteFlag
     * @return
     */
    Optional<OrderInvoice> findByOrderNoAndDelFlag(String orderNo, DeleteFlag deleteFlag);

    /**
     * 批量开票 开票状态 0待开票 1 已开票
     * @param orderInvoiceIds
     * @param invoiceTime
     * @return
     */
    @Modifying
    @Query("update OrderInvoice c set c.invoiceState = 1 ,c.invoiceTime = :invoiceTime ,c.updateTime = :invoiceTime where c.invoiceState = 0 and c.delFlag = 0 and c.orderInvoiceId in :orderInvoiceIds")
    int checkInvoiceState(@Param("orderInvoiceIds") List<String> orderInvoiceIds, @Param("invoiceTime") LocalDateTime
            invoiceTime);

    /**
     * 发票作废 是否作废 0 否 1 是
     * @param orderInvoiceId
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update OrderInvoice c set c.invoiceState = 0,c.updateTime = :updateTime, c.invoiceTime = null where c.invoiceState = 1 and c.orderInvoiceId = :orderInvoiceId")
    int invalidInvoice(@Param("orderInvoiceId") String orderInvoiceId, @Param("updateTime") LocalDateTime updateTime);


    List<OrderInvoice> findByDelFlagAndOrderNo(DeleteFlag delFlag, String orderNo);


    /**
     * 根据id查询开票信息
     * @param orderInvoiceId orderInvoiceId
     * @param delFlag delFlag
     * @return Optional<OrderInvoice>
     */
    Optional<OrderInvoice> findByOrderInvoiceIdAndDelFlag(String orderInvoiceId, DeleteFlag delFlag);

    /**
     * 根据ID删除订单开票信息
     * @param orderInvoiceId
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update OrderInvoice c set c.delFlag = 1,c.updateTime = :updateTime where c.delFlag = 0 and c.orderInvoiceId = :orderInvoiceId")
    int deleteOrderInvoice(@Param("orderInvoiceId") String orderInvoiceId, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据订单号删除订单开票
     * @param orderNo
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update OrderInvoice c set c.delFlag = 1,c.updateTime = :updateTime where c.delFlag = 0 and c.orderNo = :orderNo")
    int deleteOrderInvoiceByOrderNo(@Param("orderNo") String orderNo, @Param("updateTime") LocalDateTime updateTime);

}
