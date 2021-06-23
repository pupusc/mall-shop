package com.wanmi.sbc.customer.distribution.repository;

import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDistributionInviteNewRepository extends JpaRepository<InviteNewRecord, String>,
        JpaSpecificationExecutor<InviteNewRecord> {

    /**
     * 查询客户有效邀新数量
     *
     * @param requestCustomerId
     * @param rewardFlag
     * @param availableDistribution
     * @return
     */
    @Query("select count(i.invitedNewCustomerId) from InviteNewRecord i where i.requestCustomerId = " +
            ":requestCustomerId and i.rewardFlag = :rewardFlag and i.availableDistribution = :availableDistribution")
    Long countRecordsByRequestCustomerIdAndAvailableDistribution(@Param("requestCustomerId") String
                                                                         requestCustomerId, @Param("rewardFlag")
                                                                         RewardFlag rewardFlag, @Param
            ("availableDistribution") InvalidFlag availableDistribution);

    /**
     * 查询客户有效邀新数量
     *
     * @param requestCustomerId
     * @param availableDistribution
     * @return
     */
    @Query("select count(distinct i.invitedNewCustomerId) from InviteNewRecord i where i.requestCustomerId = " +
            ":requestCustomerId and i.availableDistribution = :availableDistribution")
    Long countRecordsByRequestCustomerIdAndAvailableDistribution(@Param("requestCustomerId") String
                                                                         requestCustomerId,  @Param
                                                                         ("availableDistribution") InvalidFlag availableDistribution);

    /**
     * 查询客户奖励已入账邀新数量
     *
     * @param requestCustomerId
     * @param rewardFlag
     * @param rewardRecorded
     * @return
     */
    @Query("select count(i.invitedNewCustomerId) from InviteNewRecord i where i.requestCustomerId = " +
            ":requestCustomerId and i.rewardFlag = :rewardFlag and i.rewardRecorded = :rewardRecorded")
    Long countRecordsByRequestCustomerIdAndRewardRecorded(@Param("requestCustomerId") String requestCustomerId,
                                                          @Param("rewardFlag") RewardFlag rewardFlag, @Param
                                                                  ("rewardRecorded") InvalidFlag rewardRecorded);

    /**
     * 根据邀新会员的id查询邀新记录
     *
     * @param invitedNewCustomerId
     * @return
     */
    List<InviteNewRecord> queryByInvitedNewCustomerId(String invitedNewCustomerId);

    /**
     * 根据邀请人ID、入账类型统计
     *
     * @param requestCustomerId
     * @param rewardFlag
     * @return
     */
    @Query("select count(i.invitedNewCustomerId) from InviteNewRecord i where i.requestCustomerId = " +
            ":requestCustomerId and i.rewardFlag = :rewardFlag")
    Long countByRequestCustomerIdAndRewardFlag(@Param("requestCustomerId") String requestCustomerId, @Param
            ("rewardFlag") RewardFlag rewardFlag);


    /**
     * 根据邀请人ID、入账类型统计
     *
     * @param requestCustomerId
     * @return
     */
    @Query("select count(distinct i.invitedNewCustomerId) from InviteNewRecord i where i.requestCustomerId = " +
            ":requestCustomerId")
    Long countByRequestCustomerId(@Param("requestCustomerId") String requestCustomerId);

    /**
     * 根据邀请人会员id查询邀新记录
     *
     * @param requestCustomerId
     * @return
     */
    List<InviteNewRecord> findByRequestCustomerId(String requestCustomerId);

    /**
     * 根据受邀人-会员ID查询邀新记录
     *
     * @param invitedNewCustomerId
     * @return
     */
    List<InviteNewRecord> findByInvitedNewCustomerId(String invitedNewCustomerId);

    /**
     * 根据受邀人会员ID、入账类型查询邀新记录
     *
     * @param invitedNewCustomerId
     * @param rewardFlag
     * @return
     */
    InviteNewRecord findByInvitedNewCustomerIdAndRewardFlag(String invitedNewCustomerId, RewardFlag rewardFlag);

    /**
     * 根据条件分页查询邀新记录
     *
     * @param availableDistribution
     * @param pageable
     * @return
     */
    @Query("select d from InviteNewRecord d where d.requestCustomerId = ?1 and d" +
            ".availableDistribution = ?2 group by d.invitedNewCustomerId")
    Page<InviteNewRecord> findInviteNewRecordPageByAvailableDistribution(String requestCustomerId, InvalidFlag availableDistribution, Pageable
            pageable);

    /**
     * 根据条件分页查询邀新记录
     *
     * @param pageable
     * @return
     */
    @Query("select d from InviteNewRecord d where d" +
            ".requestCustomerId = ?1 group by d.invitedNewCustomerId")
    Page<InviteNewRecord> findInviteNewRecordPage(String requestCustomerId, Pageable
            pageable);
}
