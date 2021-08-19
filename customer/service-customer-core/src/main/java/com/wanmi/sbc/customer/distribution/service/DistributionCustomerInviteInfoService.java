package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewListRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoQueryRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoReviseRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorCustomerInviteInfoUpdateRequest;
import com.wanmi.sbc.customer.bean.enums.FailReasonFlag;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.distribution.repository.CustomerDistributionInviteNewRepository;
import com.wanmi.sbc.customer.distribution.repository.DistributionCustomerInviteInfoRepository;
import com.wanmi.sbc.customer.util.QueryConditionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>分销员邀新信息逻辑</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Service("DistributionCustomerInviteInfoService")
public class DistributionCustomerInviteInfoService {

    @Autowired
    private DistributionCustomerInviteInfoRepository distributionCustomerInviteInfoRepository;

    @Autowired
    private CustomerDistributionInviteNewRepository customerDistributionInviteNewRepository;
    /**
     * 新增分销员邀新信息
     * @author lq
     */
    @Transactional
    public DistributionCustomerInviteInfo add(DistributionCustomerInviteInfo entity) {
        distributionCustomerInviteInfoRepository.save(entity);
        return entity;
    }
    /**
     * 获取新增的分销员邀新信息
     * 初始化为0
     * @return
     */
    public DistributionCustomerInviteInfo getDistributionCustomerInviteInfo(DistributionCustomer distributionCustomer) {
        DistributionCustomerInviteInfo info = new DistributionCustomerInviteInfo();
        info.setCustomerId(distributionCustomer.getCustomerId());
        info.setDistributionId(distributionCustomer.getDistributionId());
        info.setRewardCashCount(0);
        info.setRewardCashLimitCount(0);
        info.setRewardCashAvailableLimitCount(0);
        info.setRewardCouponCount(0);
        info.setRewardCouponLimitCount(0);
        info.setRewardCouponAvailableLimitCount(0);
        return info;
    }
    /**
     * 分页查询分销员邀新信息
     * @author lq
     */
    public Page<DistributionCustomerInviteInfo> page(DistributionCustomerInviteInfoQueryRequest queryReq) {
        return distributionCustomerInviteInfoRepository.findAll(
                DistributionCustomerInviteInfoWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }


    /**
     * 将实体包装成VO
     * @author lq
     */
    public DistributionCustomerInviteInfoVO wrapperVo(DistributionCustomerInviteInfo distributionCustomerInviteInfo) {
        if (distributionCustomerInviteInfo != null) {
            DistributionCustomerInviteInfoVO distributionCustomerVO = new DistributionCustomerInviteInfoVO();
            KsBeanUtil.copyPropertiesThird(distributionCustomerInviteInfo, distributionCustomerVO);
            return distributionCustomerVO;
        }
        return null;
    }

    /**
     * 更新邀新信息
     * count+
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public int updatCount(DistributorCustomerInviteInfoUpdateRequest request) {
       return distributionCustomerInviteInfoRepository.updatCount(request);
    }


    /**
     * 补发更新邀新信息
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void afterSupplyAgainUpdate(DistributorCustomerInviteInfoUpdateRequest request) {
        distributionCustomerInviteInfoRepository.afterSupplyAgainUpdate(request);
    }


    /**
     * 校正分销员邀新信息相关信息
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void revise(DistributorCustomerInviteInfoReviseRequest request) {
        //查询邀新数据
        DistributionInviteNewListRequest listRequest = new DistributionInviteNewListRequest();
        listRequest.setRequestCustomerId(request.getCustomerId());
        List<InviteNewRecord> inviteNewRecords =
                customerDistributionInviteNewRepository.findAll(QueryConditionsUtil.getWhereCriteria(listRequest));
        //发放邀新奖金人数
        Long rewardCashCount = inviteNewRecords.stream().filter(vo->InvalidFlag.YES.equals(vo.getRewardRecorded()) ).filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag())).count();
        //未放邀新奖金人数
        Long rewardCashLimitCount = inviteNewRecords.stream().filter(vo->InvalidFlag.NO.equals(vo.getRewardRecorded()) ).filter(vo->FailReasonFlag.LIMITED.equals(vo.getFailReasonFlag())).filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag()) ).count();
        //未放邀新奖金人数--有效邀新有几个
        Long rewardCashAvailableLimitCount = inviteNewRecords.stream().filter(vo->InvalidFlag.NO.equals(vo.getRewardRecorded()) ).filter(vo->FailReasonFlag.LIMITED.equals(vo.getFailReasonFlag())).filter(vo -> RewardFlag.CASH.equals(vo.getRewardFlag()) && InvalidFlag.YES.equals(vo.getAvailableDistribution())).count();
         //发放邀新优惠券人数
        Long rewardCouponCount = inviteNewRecords.stream().filter(vo->InvalidFlag.YES.equals(vo.getRewardRecorded()) ).filter(vo -> RewardFlag.COUPON.equals(vo.getRewardFlag())).count();
        //未发放邀新优惠券人数
        Long rewardCouponLimitCount = inviteNewRecords.stream().filter(vo->InvalidFlag.NO.equals(vo.getRewardRecorded()) ).filter(vo->FailReasonFlag.LIMITED.equals(vo.getFailReasonFlag())).filter(vo -> RewardFlag.COUPON.equals(vo.getRewardFlag())).count();
        //未发放邀新优惠券人数--有效邀新有几个
        Long rewardCouponAvailableLimitCount = inviteNewRecords.stream().filter(vo->InvalidFlag.NO.equals(vo.getRewardRecorded()) ).filter(vo->FailReasonFlag.LIMITED.equals(vo.getFailReasonFlag())).filter(vo -> RewardFlag.COUPON.equals(vo.getRewardFlag()) && InvalidFlag.YES.equals(vo.getAvailableDistribution())).count();

        DistributionCustomerInviteInfo info = distributionCustomerInviteInfoRepository.findByCustomerId(request.getCustomerId());
        info.setCustomerId(request.getCustomerId());
        info.setDistributionId(request.getDistributionId());
        info.setRewardCashCount(rewardCashCount.intValue());
        info.setRewardCashLimitCount(rewardCashLimitCount.intValue());
        info.setRewardCashAvailableLimitCount(rewardCashAvailableLimitCount.intValue());
        info.setRewardCouponCount(rewardCouponCount.intValue());
        info.setRewardCouponLimitCount(rewardCouponLimitCount.intValue());
        info.setRewardCouponAvailableLimitCount(rewardCouponAvailableLimitCount.intValue());
        distributionCustomerInviteInfoRepository.save(info);
    }

    /**
     * 根据会员ID查询分销员邀新记录-任务
     * @param customerId
     * @return
     */
    public DistributionCustomerInviteInfo findByCustomerId(String customerId){
        return distributionCustomerInviteInfoRepository.findByCustomerId(customerId);
    }


}
