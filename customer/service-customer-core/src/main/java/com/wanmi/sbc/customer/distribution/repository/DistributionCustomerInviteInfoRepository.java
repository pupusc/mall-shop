package com.wanmi.sbc.customer.distribution.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoUpdateRequest;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>分销员DAO</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Repository
public interface DistributionCustomerInviteInfoRepository extends JpaRepository<DistributionCustomerInviteInfo, String>,
        JpaSpecificationExecutor<DistributionCustomerInviteInfo> {


    @Modifying
    @Query("update DistributionCustomerInviteInfo c set " +
            // 已发放邀新奖励现金人数
            "c.rewardCashCount = c.rewardCashCount + :#{#request.rewardCashCount}," +
            // 达到上限未发放奖励现金人数
            "c.rewardCashLimitCount = c.rewardCashLimitCount + :#{#request.rewardCashLimitCount}," +
            // 达到上限未发放奖励现金有效邀新人数
            "c.rewardCashAvailableLimitCount = c.rewardCashAvailableLimitCount + :#{#request.rewardCashAvailableLimitCount}," +
            // 已发放邀新奖励优惠券数
            "c.rewardCouponCount = c.rewardCouponCount + :#{#request.rewardCouponCount}," +
            // 达到上限未发放奖励优惠券数
            "c.rewardCouponLimitCount = c.rewardCouponLimitCount + :#{#request.rewardCouponLimitCount}," +
            // 达到上限未发放奖励优惠券有效邀新人数
            "c.rewardCouponAvailableLimitCount = c.rewardCouponAvailableLimitCount + :#{#request.rewardCouponAvailableLimitCount}" +
            " where c.distributionId = :#{#request.distributeId}")
    int updatCount(@Param("request") DistributorCustomerInviteInfoUpdateRequest request);


    @Modifying
    @Query("update DistributionCustomerInviteInfo c set " +
            // 已发放邀新奖励现金人数
            "c.rewardCashCount = c.rewardCashCount + :#{#request.rewardCashCount}," +
            // 达到上限未发放奖励现金人数
            "c.rewardCashLimitCount = c.rewardCashLimitCount - :#{#request.rewardCashLimitCount}," +
            // 达到上限未发放奖励现金有效邀新人数
            "c.rewardCashAvailableLimitCount = c.rewardCashAvailableLimitCount - :#{#request.rewardCashAvailableLimitCount}," +
            // 已发放邀新奖励优惠券数
            "c.rewardCouponCount = c.rewardCouponCount + :#{#request.rewardCouponCount}," +
            // 达到上限未发放奖励优惠券数
            "c.rewardCouponLimitCount = c.rewardCouponLimitCount - :#{#request.rewardCouponLimitCount}," +
            // 达到上限未发放奖励优惠券有效邀新人数
            "c.rewardCouponAvailableLimitCount = c.rewardCouponAvailableLimitCount - :#{#request.rewardCouponAvailableLimitCount}" +
            " where c.distributionId = :#{#request.distributeId}")
    int afterSupplyAgainUpdate(@Param("request") DistributorCustomerInviteInfoUpdateRequest request);



    /**
     * 根据会员编号查询单个查询分销员邀新信息
     *
     * @param customerId
     * @return
     */
    DistributionCustomerInviteInfo findByCustomerId(String customerId);

}
