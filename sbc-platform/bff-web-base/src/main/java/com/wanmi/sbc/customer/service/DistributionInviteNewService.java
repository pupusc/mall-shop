package com.wanmi.sbc.customer.service;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewProvider;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewAddRegisterRequest;
import com.wanmi.sbc.customer.bean.dto.DistributionRewardCouponDTO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.distribute.dto.InviteRegisterDTO;
import com.wanmi.sbc.marketing.bean.enums.DistributionLimitType;
import com.wanmi.sbc.marketing.bean.enums.RewardCashType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionRewardCouponVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 分销邀新service
 * @Autho qiaokang
 * @Date：2019-03-05 13:44:51
 */
@Slf4j
@Service
public class DistributionInviteNewService {

    /**
     * 注入分销员操作Provider
     */
    @Autowired
    private DistributionInviteNewProvider distributionInviteNewProvider;

    /**
     * 注入分销设置缓存service
     */
    @Autowired
    private DistributionCacheService distributionCacheService;

    /**
     * 注入用户资金provider
     */
    @Autowired
    CustomerFundsProvider customerFundsProvider;

    /**
     * 新增邀新记录
     * 受邀人id和邀请人id都存在时，才会新增
     * @param customerId        受邀人id
     * @param requestCustomerId 邀请人id
     */
    public void addRegisterInviteNewRecord(String customerId, String requestCustomerId, TerminalSource terminalSource){
        if (StringUtils.isBlank(requestCustomerId) || StringUtils.isBlank(customerId)) {
            log.info("邀请人id：{}、受邀人id：{}，不用新增邀新记录",requestCustomerId,customerId);
            return;
        }
        log.info("===================新增邀新记录开始，邀请人id：{}、受邀人id：{}================ " , requestCustomerId,customerId);

        DistributionInviteNewAddRegisterRequest addRegisterRequest = new DistributionInviteNewAddRegisterRequest();
        //邀请人id
        addRegisterRequest.setRequestCustomerId(requestCustomerId);

        //受邀人id
        addRegisterRequest.setCustomerId(customerId);

        //查询是否开启社交分销
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        addRegisterRequest.setOpenFlag(openFlag);

        // 查询是否开启邀新奖励
        DefaultFlag inviteFlag = distributionCacheService.queryInviteFlag();
        addRegisterRequest.setInviteFlag(inviteFlag);

        // 查询是否开启邀新奖励限制 0：不限，1：仅限有效邀新
        DistributionLimitType distributionLimitType = distributionCacheService.queryRewardLimitType();
        addRegisterRequest.setDistributionLimitType(Objects.nonNull(distributionLimitType) ? DefaultFlag.fromValue(distributionLimitType.toValue()) : null);

        //查询是否开启奖励现金开关
        DefaultFlag rewardCashFlag = distributionCacheService.getRewardCashFlag();
        addRegisterRequest.setRewardCashFlag(rewardCashFlag);

        // 后台配置的奖励金额
        BigDecimal settingAmount = distributionCacheService.queryRewardCash();
        addRegisterRequest.setSettingAmount(settingAmount);


        // 奖励上限类型设置
        RewardCashType rewardCashType = distributionCacheService.queryRewardCashType();
        addRegisterRequest.setRewardCashType(Objects.nonNull(rewardCashType) ? DefaultFlag.fromValue(rewardCashType.toValue()) : null);

        // 奖励现金上限(人数)
        Integer rewardCashCount = distributionCacheService.queryRewardCashCount();
        addRegisterRequest.setRewardCashCount(rewardCashCount);

        // 是否开启奖励优惠券
        DefaultFlag rewardCouponFlag= distributionCacheService.getRewardCouponFlag();
        addRegisterRequest.setRewardCouponFlag(rewardCouponFlag);

        //优惠券信息
        List<CouponInfoVO> couponInfoVOList = distributionCacheService.getCouponInfos();
        if(CollectionUtils.isNotEmpty(couponInfoVOList)){
            BigDecimal denominationSum  = couponInfoVOList.stream().map(CouponInfoVO::getDenomination).reduce(BigDecimal::add).get();
            List<String> couponNameList = couponInfoVOList.stream().map(CouponInfoVO::getCouponName).collect(Collectors.toList());
            addRegisterRequest.setCouponNameList(couponNameList);
            addRegisterRequest.setDenominationSum(denominationSum);
        }

        //优惠券组数信息
        List<DistributionRewardCouponVO> distributionRewardCouponVOList = distributionCacheService.getCouponInfoCounts();
        List<DistributionRewardCouponDTO> distributionRewardCouponDTOList = CollectionUtils.isEmpty(distributionRewardCouponVOList) ? null : KsBeanUtil.convert(distributionRewardCouponVOList,DistributionRewardCouponDTO.class);
        addRegisterRequest.setDistributionRewardCouponDTOList(distributionRewardCouponDTOList);

        //"奖励优惠券上限(组数)
        Integer rewardCouponCount= distributionCacheService.getRewardCouponCount();
        addRegisterRequest.setRewardCouponCount(rewardCouponCount);

        // 2.判断是否可以升级
        InviteRegisterDTO inviteRegisterDTO = distributionCacheService.getInviteRegisterDTO();
        DefaultFlag enableFlag = inviteRegisterDTO.getEnableFlag();
        addRegisterRequest.setEnableFlag(enableFlag);
        distributionLimitType = inviteRegisterDTO.getLimitType();
        addRegisterRequest.setLimitType(Objects.nonNull(distributionLimitType) ? DefaultFlag.fromValue(distributionLimitType.toValue()) : null);
        Integer inviteCount = inviteRegisterDTO.getInviteCount();
        addRegisterRequest.setInviteCount(inviteCount);

        //分销等级设置信息
        addRegisterRequest.setDistributorLevelVOList(distributionCacheService.getDistributorLevels());
        //基础邀新奖励限制
        distributionLimitType = distributionCacheService.getBaseLimitType();
        addRegisterRequest.setBaseLimitType(Objects.nonNull(distributionLimitType) ? DefaultFlag.fromValue(distributionLimitType.toValue()) : null);
        addRegisterRequest.setTerminalSource(terminalSource);
        log.info("===================新增邀新记录DistributionInviteNewAddRegisterRequest对象组装完成：{}================ " , addRegisterRequest);

        distributionInviteNewProvider.addRegister(addRegisterRequest);
    }

}
