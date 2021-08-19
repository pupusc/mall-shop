package com.wanmi.sbc.crm.customerplan.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanMapper;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanSendCountMapper;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanSendMapper;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSend;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeListByConditionResponse;
import com.wanmi.sbc.marketing.bean.dto.CouponCodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSendCount;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanSendCountVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>权益礼包优惠券发放统计表业务逻辑</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@Service("CustomerPlanSendCountService")
@Slf4j
public class CustomerPlanSendCountService {
	@Autowired
	private CustomerPlanSendCountMapper customerPlanSendCountRepository;

	@Autowired
	private CustomerPlanSendMapper customerPlanSendMapper;

	@Autowired
	private CustomerPlanMapper customerPlanMapper;

	@Autowired
	private CouponCodeQueryProvider couponCodeQueryProvider;


	/** 
	 * 新增权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	@Transactional
	public CustomerPlanSendCount add(CustomerPlanSendCount entity) {
		customerPlanSendCountRepository.insertSelective(entity);
		return entity;
	}
	
	/** 
	 * 修改权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	@Transactional
	public CustomerPlanSendCount modify(CustomerPlanSendCount entity) {
		customerPlanSendCountRepository.updateByPrimaryKeySelective(entity);
		return entity;
	}

	/**
	 * 单个删除权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	@Transactional
	public void deleteById(Long id) {
		customerPlanSendCountRepository.deleteByPrimaryKey(id);
	}


	@Transactional
	public void deleteByPlanId(Long planId) {
		customerPlanSendCountRepository.deleteByPlanId(planId);
	}
    @Transactional
	public void deleteAll() {
	    customerPlanSendCountRepository.deleteAll();
    }
	/** 
	 * 批量删除权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> customerPlanSendCountRepository.deleteByPrimaryKey(id));
	}
	
	/** 
	 * 单个查询权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	public CustomerPlanSendCount getById(Long id){
		return customerPlanSendCountRepository.selectByPrimaryKey(id);
	}

	/**
	 * 根据运营计划id单个查询权益礼包优惠券发放统计表
	 * @author zhanghao
	 */
	public CustomerPlanSendCount getByPlanId(Long planId) {
		return customerPlanSendCountRepository.selectByPlanId(planId);
	}

	/**
	 * 将实体包装成VO
	 * @author zhanghao
	 */
	public CustomerPlanSendCountVO wrapperVo(CustomerPlanSendCount customerPlanSendCount) {
		if (customerPlanSendCount != null){
			CustomerPlanSendCountVO customerPlanSendCountVO=new CustomerPlanSendCountVO();
			KsBeanUtil.copyPropertiesThird(customerPlanSendCount,customerPlanSendCountVO);
			return customerPlanSendCountVO;
		}
		return null;
	}

	@Transactional
	public void generator() {
		List<Long> planIds = customerPlanMapper.selectPlanningIds();
		if (CollectionUtils.isNotEmpty(planIds)) {
			planIds.forEach(id -> {
				//统计礼包发放人数（去重）
				Long giftPersonCount = customerPlanSendMapper.selectGiftCount(CustomerPlanSend.builder()
						.isOnlyCoupon(BigDecimal.ROUND_DOWN)
						.isRepeat(BigDecimal.ROUND_DOWN)
						.planId(id)
						.build());
				//统计礼包发放次数（不去重）
				Long giftCount = customerPlanSendMapper.selectGiftCount(CustomerPlanSend.builder()
						.isOnlyCoupon(BigDecimal.ROUND_DOWN)
						.isRepeat(BigDecimal.ROUND_UP)
						.planId(id)
						.build());
				//统计优惠券发放人数（去重）
				Long couponPersonCount = customerPlanSendMapper.selectGiftCount(CustomerPlanSend.builder()
						.isOnlyCoupon(BigDecimal.ROUND_UP)
						.isRepeat(BigDecimal.ROUND_DOWN)
						.planId(id)
						.build());
				//统计优惠券发放人次数（不去重）
				Long couponCount = customerPlanSendMapper.selectGiftCount(CustomerPlanSend.builder()
						.isOnlyCoupon(BigDecimal.ROUND_UP)
						.isRepeat(BigDecimal.ROUND_UP)
						.planId(id)
						.build());
				//计算发放优惠券张数
				couponCount = ObjectUtils.defaultIfNull(customerPlanMapper.selectSumGiftCount(id), 0L) * couponCount;
				//根据运营计划id查询运营计划
				CustomerPlan customerPlan = customerPlanMapper.selectByPrimaryKey(id);

				long couponPersonUseCount = 0L;
				long couponUseCount = 0L;
				double couponUseRate = 0.00;

				if (StringUtils.isNotEmpty(customerPlan.getActivityId())) {
					BaseResponse<CouponCodeListByConditionResponse> response =
							couponCodeQueryProvider.listCouponCodeByCondition(CouponCodeQueryRequest.builder()
									.useStatus(DefaultFlag.YES)
									.activityId(customerPlan.getActivityId())
									.build());
					if(StringUtils.equals(response.getCode(), CommonErrorCode.SUCCESSFUL)){
						List<CouponCodeDTO> couponCodeList = response.getContext().getCouponCodeList();
						if(CollectionUtils.isNotEmpty(couponCodeList)) {
							couponUseCount = (long) couponCodeList.size();
							// 根据customerId去重
							List<String> collect =
									couponCodeList.parallelStream().map(CouponCodeDTO::getCustomerId).distinct().collect(Collectors.toList());
							couponPersonUseCount = (long) collect.size();
							couponUseRate = ((double)couponPersonUseCount / (double)couponPersonCount) * 100;
							couponUseRate = (double) Math.round(couponUseRate * 100) / 100;
						}
					} else {
						throw new SbcRuntimeException("查询优惠券接口失败！！");
					}
				}

				//删除原有的统计
				deleteByPlanId(id);
				//重新加入新的统计结果
				add(CustomerPlanSendCount.builder()
						.planId(id)
						.giftPersonCount(giftPersonCount)
						.giftCount(giftCount)
						.couponPersonCount(couponPersonCount)
						.couponCount(couponCount)
						.couponPersonUseCount(couponPersonUseCount)
						.couponUseCount(couponUseCount)
						.couponUseRate(couponUseRate)
						.createTime(LocalDateTime.now())
						.build());
			});
		}
	}
}
