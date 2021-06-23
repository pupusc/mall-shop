package com.wanmi.sbc.account.customerdrawcash.repository;

import com.wanmi.sbc.account.bean.enums.AuditStatus;
import com.wanmi.sbc.account.bean.enums.CustomerOperateStatus;
import com.wanmi.sbc.account.bean.enums.DrawCashStatus;
import com.wanmi.sbc.account.bean.enums.FinishStatus;
import com.wanmi.sbc.account.customerdrawcash.model.root.CustomerDrawCash;
import com.wanmi.sbc.common.enums.DeleteFlag;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员提现管理DAO</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@Repository
public interface CustomerDrawCashRepository extends JpaRepository<CustomerDrawCash, String>,
        JpaSpecificationExecutor<CustomerDrawCash> {

    @Modifying
    @Query("update CustomerDrawCash set auditStatus = ?2,rejectReason = ?3, finishStatus=?4,drawCashStatus=?5," +
            "drawCashFailedReason=?6,accountBalance = ?7 , finishTime = now() where drawCashId in (?1)")
    void updateDrawCashAuditStatusBatch(@Param("drawCashIdList") List<String> drawCashIdList,
                                        @Param("auditStatus") AuditStatus auditStatus,
                                        @Param("rejectReason") String rejectReason,
                                        @Param("finishStatus")FinishStatus finishStatus,
                                        @Param("drawCashStatus")DrawCashStatus drawCashStatus,
                                        @Param("drawCashFailedReason")String drawCashFailedReason,
                                               @Param("accountBalance")  BigDecimal accountBalance);

    /**
     * 会员当天提现总金额，（已申请、待审核的和已提现成功的记录）
     * @param customerId
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query("SELECT sum(drawCashSum) from CustomerDrawCash where customerId = ?1 " +
            "AND applyTime >= ?2  AND applyTime <= ?3 and ((auditStatus = 0 and customerOperateStatus = 0) or drawCashStatus = 2)")
    BigDecimal countDrawCashSum(@Param("customerId") String customerId,
                                @Param("beginTime") LocalDateTime beginTime,
                                @Param("endTime") LocalDateTime endTime);


    @Query("SELECT count(drawCashId) from CustomerDrawCash where auditStatus=?1 AND drawCashStatus=?2 AND " +
            "customerOperateStatus=?3 AND finishStatus=?4 AND delFlag=?5 ")
    Integer countDrawCashTabNum(@Param("auditStatus") AuditStatus auditStatus,
                                @Param("drawCashStatus")DrawCashStatus drawCashStatus,
                                @Param("customerOperateStatus") CustomerOperateStatus customerOperateStatus,
                                @Param("finishStatus")FinishStatus finishStatus,
                                @Param("delFlag") DeleteFlag deleteFlag);



    /**
     * 根据会员ID更新会员账号
     *
     * @param customerId      会员ID
     * @param customerAccount 会员账号
     * @param updateTime      更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerDrawCash cdc set cdc.customerAccount = :customerAccount,cdc.updateTime = :updateTime where cdc.customerId = :customerId")
    int updateCustomerAccountByCustomerId(@Param("customerId") String customerId,
                                          @Param("customerAccount") String customerAccount,
                                          @Param("updateTime") LocalDateTime updateTime);



    /**
     * 根据会员ID更新会员名称
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @param updateTime   更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerDrawCash cdc set cdc.customerName = ?2 ,cdc.updateTime = ?3 where cdc.customerId = ?1")
    int updateCustomerNameByCustomerId(@Param("customerId") String customerId,
                                       @Param("customerName") String customerName,
                                       @Param("updateTime") LocalDateTime updateTime);


}
