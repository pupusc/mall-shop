package com.wanmi.sbc.customer.redis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanmi.sbc.customer.api.constant.DistributionCommissionRedisKey;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerModifyRewardRequest;
import com.wanmi.sbc.customer.api.request.distribution.AfterSettleUpdateDistributorRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分销佣金-Redis服务层
 */
@Slf4j
@Service
public class DistributionCommissionRedisService {

    @Autowired
    private RedisService redisService;

    /**
     * 更新分销员奖励信息--分销佣金统计--redis同步
     * @param request
     * @return
     */
    public Map<String, Double> modifyReward(DistributionCustomerModifyRewardRequest request){
        Double commissionTotal = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL, request.getRewardCash().doubleValue());
        Double rewardCash = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH, request.getRewardCash().doubleValue());
        Double rewardCashNotRecorded = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED, request.getRewardCashNotRecorded().doubleValue());
        Double commissionNotRecorded = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED, request.getCommissionNotRecorded().doubleValue());
        return ImmutableMap.of(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,commissionTotal,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH,rewardCash,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED,rewardCashNotRecorded,
                DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED,commissionNotRecorded);
    }


    /**
     * 分销佣金结算后更新分销员统计相关信息--reids同步
     * @param request
     * @return
     */
    public Map<String, Double> afterSettleUpdate(AfterSettleUpdateDistributorRequest request){
        Double commissionTotal = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,
                request.getGrantAmount().doubleValue()+request.getInviteAmount().doubleValue());
        Double rewardCash = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH,
                request.getInviteAmount().doubleValue());
        Double commission = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION,
                request.getGrantAmount().doubleValue());
        Double rewardCashNotRecorded = redisService.decrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED,
                request.getInviteAmount().doubleValue());
        Double commissionNotRecorded = redisService.decrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED,
                request.getTotalDistributeAmount().doubleValue());
        return ImmutableMap.of(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,commissionTotal,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH,rewardCash,
                DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION,commission,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED,rewardCashNotRecorded,
                DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED,commissionNotRecorded);
    }


    /**
     * 补发邀新奖励--redis同步
     * @param amout
     * @return
     */
    public Map<String, Double> afterSupplyAgainUpdate(BigDecimal amout){
        Double commissionTotal = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,amout.doubleValue());
        Double rewardCash = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH, amout.doubleValue());
        Double rewardCashNotRecorded = redisService.decrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED, amout.doubleValue());
        return ImmutableMap.of(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,commissionTotal,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH,rewardCash,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED,rewardCashNotRecorded);
    }


    /**
     * 初始化分销员佣金的各个统计数据
     * @param commissitonTotal
     * @param commission
     * @param rewardCash
     * @param commissionNotRecorded
     * @param rewardCashNotRecorded
     * @return
     */
    public Boolean initDistributionCommissionStatistics(Double commissitonTotal, Double commission,
                                                                    Double rewardCash,Double commissionNotRecorded,
                                                                    Double rewardCashNotRecorded) {
        List list = ImmutableList.of(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL,DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH,DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED,
                DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED);
        redisService.del(list);
        commissitonTotal = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_TOTAL, commissitonTotal);
        commission = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION, commission);
        rewardCash = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH, rewardCash);
        commissionNotRecorded = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_COMMISSION_NOTRECORDED, commissionNotRecorded);
        rewardCashNotRecorded = redisService.incrByFloat(DistributionCommissionRedisKey.DISTRIBUTION_REWARD_CASH_NOTRECORDED, rewardCashNotRecorded);
        return Boolean.TRUE;
    }
}
