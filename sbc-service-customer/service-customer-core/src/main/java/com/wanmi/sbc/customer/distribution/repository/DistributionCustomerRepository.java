package com.wanmi.sbc.customer.distribution.repository;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.distribution.AfterSettleUpdateDistributorRequest;
import com.wanmi.sbc.customer.model.root.DistributionCommissionStatistics;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>分销员DAO</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Repository
public interface DistributionCustomerRepository extends JpaRepository<DistributionCustomer, String>,
        JpaSpecificationExecutor<DistributionCustomer> {


    /**
     * 根据会员编号查询单个查询分销员
     *
     * @param customerId
     * @param deleteFlag
     * @return
     */
    DistributionCustomer findByCustomerIdAndDelFlag(String customerId, DeleteFlag deleteFlag);

    /**
     * 根据会员编号查询单个查询分销员
     *
     * @param customerId
     * @return
     */
    DistributionCustomer findByCustomerId(String customerId);

    /**
     * 根据会员编号列表查询分销员列表
     *
     * @param customerId
     * @param deleteFlag
     * @return
     */
    @Query(value = "from DistributionCustomer d where d.customerId in ?1 and d.delFlag = ?2")
    List<DistributionCustomer> findByCustomerIdsAndDelFlag(List<String> customerId, DeleteFlag deleteFlag);

    /**
     * 根据会员编号查询单个查询有效分销员
     *
     * @param customerId
     * @param deleteFlag
     * @param distributorFlag
     * @return
     */
    DistributionCustomer findByCustomerIdAndDistributorFlagAndDelFlag(String customerId, DefaultFlag distributorFlag,
                                                                      DeleteFlag deleteFlag);
    /**
     * 根据被邀请会员编号查询单个查询有效分销员
     *
     * @param inviteCustomerId
     * @param deleteFlag
     * @param distributorFlag
     * @return
     */
    @Query("SELECT d.distributionId, d.inviteCode, d.inviteCustomerIds FROM DistributionCustomer d WHERE d" +
            ".distributorFlag = :distributorFlag and d.delFlag = :deleteFlag and d" +
            ".inviteCustomerIds " +
            "LIKE CONCAT('%'," +
            ":inviteCustomerId," +
            "'%')")
    DistributionCustomer findByInviteCustomerId(@Param("inviteCustomerId") String inviteCustomerId,
                                                @Param("distributorFlag") DefaultFlag distributorFlag,
                                                @Param("deleteFlag") DeleteFlag deleteFlag);
    /**
     * 根据分销员编号查询单个查询分销员
     *
     * @param distributionId
     * @param deleteFlag
     * @return
     */
    DistributionCustomer findByDistributionIdAndDelFlag(String distributionId, DeleteFlag deleteFlag);

    /**
     * 批量启用/禁用分销员
     *
     * @param forbiddenFlag
     * @param distributionIds
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.forbiddenFlag = :forbiddenFlag, c.forbiddenReason = :forbidReason where c" +
            ".delFlag = 0 and c.distributionId in :distributionIds")
    int updateForbiddenFlag(@Param("forbiddenFlag") DefaultFlag forbiddenFlag, @Param("distributionIds")
            List<String> distributionIds, @Param("forbidReason") String forbidReason);


    /**
     * 根据会员ID更新会员账号
     *
     * @param customerId      会员ID
     * @param customerAccount 会员账号
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.customerAccount = :customerAccount where c.customerId = :customerId")
    int updateCustomerAccountByCustomerId(@Param("customerId") String customerId, @Param("customerAccount") String customerAccount);

    /**
     * 根据会员ID更新会员名称
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.customerName = :customerName  where c.customerId = :customerId")
    int updateCustomerNameByCustomerId(@Param("customerId") String customerId, @Param("customerName") String customerName);

    /**
     * 根据会员ID更新会员名称和会员账号
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.customerName = :customerName ,c.customerAccount=:customerAccount where c.customerId = :customerId")
    int updateCustomerNameAndAccountByCustomerId(@Param("customerId") String customerId, @Param("customerAccount") String customerAccount, @Param("customerName") String customerName);

    /**
     * 更新分销员奖励信息
     *
     * @param request
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.inviteCount = c.inviteCount + :inviteCount," +
            "c.rewardCash = c.rewardCash + :rewardCash,c.rewardCashNotRecorded = c.rewardCashNotRecorded + :rewardCashNotRecorded," +
            "c.distributionTradeCount = c.distributionTradeCount + :distributionTradeCount,c.sales =c.sales + :sales ," +
            "c.commissionNotRecorded = c.commissionNotRecorded +:commissionNotRecorded,c.commissionTotal = c.commissionTotal + :commissionTotal " +
            " where c.distributionId = :distributionId")
    int modifyReward(@Param("inviteCount") Integer inviteCount, @Param("rewardCash") BigDecimal rewardCash, @Param("rewardCashNotRecorded") BigDecimal rewardCashNotRecorded,
                     @Param("distributionTradeCount") Integer distributionTradeCount,@Param("sales") BigDecimal sales,@Param("commissionNotRecorded") BigDecimal commissionNotRecorded,
                     @Param("commissionTotal") BigDecimal commissionTotal,@Param("distributionId") String distributionId);

    @Modifying
    @Query("update DistributionCustomer c set c.inviteAvailableCount = c.inviteAvailableCount + :#{#request.inviteNum}," +
            "c.rewardCash = c.rewardCash + :#{#request.inviteAmount}," +
            "c.rewardCashNotRecorded = c.rewardCashNotRecorded - :#{#request.inviteAmount}," +
            "c.distributionTradeCount = c.distributionTradeCount - :#{#request.orderNum},c.sales =c.sales - :#{#request.amount} ," +
            "c.commission = c.commission + :#{#request.grantAmount},c.commissionTotal = c.commissionTotal + :#{#request.grantAmount} + :#{#request.inviteAmount}, c.commissionNotRecorded = c.commissionNotRecorded -:#{#request.totalDistributeAmount} " +
            " where c.distributionId = :#{#request.distributeId}")
    int afterSettleUpdate(@Param("request") AfterSettleUpdateDistributorRequest request);


    @Modifying
    @Query("update DistributionCustomer c set " +
            "c.rewardCash =c.rewardCash + :rewardCash, " +
            "c.commissionTotal =c.commissionTotal + :rewardCash, " +
            "c.rewardCashNotRecorded =c.rewardCashNotRecorded - :rewardCashNotRecorded " +
            " where c.distributionId = :distributeId ")
    int afterSupplyAgainUpdate(@Param("rewardCash")  BigDecimal rewardCash ,@Param("rewardCashNotRecorded")  BigDecimal rewardCashNotRecorded ,@Param("distributeId")  String distributeId );


    @Query(value = "select new com.wanmi.sbc.customer.model.root.DistributionCommissionStatistics(sum(f.commissionTotal)," +
            "sum(f.commission),sum(f.rewardCash),sum(f.commissionNotRecorded),sum(f.rewardCashNotRecorded)) FROM DistributionCustomer f ")
    DistributionCommissionStatistics statistics();

    /**
     * 根据邀请码查询分销员信息
     * @param inviteCode
     * @return
     */
    DistributionCustomer findByInviteCode(String inviteCode);

    /**
     * 更新分销员提成-已入账佣金包含提成
     */
    @Modifying
    @Query("update DistributionCustomer c set c.commission = c.commission + :commission  where c.customerId = :customerId")
    int updateCommissionByCustomerId(@Param("customerId") String customerId, @Param("commission") BigDecimal commission);

    /**
     * 查询邀请码为空的分销员集合信息
     * @return
     */
    List<DistributionCustomer> findByInviteCodeIsNull(Pageable pageable);

    /**
     * 初始化分销员的分销员等级
     */
    @Modifying
    @Query("update DistributionCustomer c set c.distributorLevelId = ?1 where c.distributorFlag = 1 and c.distributorLevelId is null")
    int initDistributorLevel(String defaultLevelId);

    /**
     * 更新客户ID
     * @param oldCustomerId
     * @param customerAccount
     * @param customerId
     * @return
     */
    @Modifying
    @Query("update DistributionCustomer c set c.customerId = :customerId  where c.customerId = :oldCustomerId")
    int modifyCustomerId(@Param("oldCustomerId")String oldCustomerId,  @Param("customerId")String customerId);
}
