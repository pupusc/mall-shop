package com.wanmi.sbc.quartz.job;

import com.google.common.base.Splitter;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerListCustomerIdByPageableRequest;
import com.wanmi.sbc.customer.api.request.customer.EnterpriseCustomerPageRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCustomerIdPageRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListCustomerIdByStoreIdRequest;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailForPageVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityGetByActivityIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchSendCouponRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponActivityDetailByActivityIdResponse;
import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigAndCouponInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.CouponInfoDTO;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityConfigVO;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.quartz.enums.TaskBizType;
import com.wanmi.sbc.quartz.enums.TaskStatus;
import com.wanmi.sbc.quartz.model.entity.TaskInfo;
import com.wanmi.sbc.quartz.service.QuartzManagerService;
import com.wanmi.sbc.quartz.service.TaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 精准发券-全平台、店铺等级、指定用户
 * @author: Geek Wang
 * @createDate: 2019/8/5 19:10
 * @version: 1.0
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class PrecisionVouchersJob implements Job {

	@Autowired
	private CouponActivityQueryProvider couponActivityQueryProvider;

	@Autowired
	private CustomerQueryProvider customerQueryProvider;

	@Autowired
	private StoreCustomerQueryProvider storeCustomerQueryProvider;

	@Autowired
	private CouponCodeProvider couponCodeProvider;

	@Autowired
	private QuartzManagerService quartzManagerService;

	@Autowired
	private TaskJobService taskJobService;

	@Autowired
	private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		String activityId = jobDataMap.getString("bizId");
		log.info("精准发券，优惠券活动ID：{},定时任务开始运行！",activityId);
		TaskInfo taskInfo = taskJobService.findByBizId(activityId);
		if (Objects.isNull(taskInfo) || taskInfo.getBizType() != TaskBizType.PRECISION_VOUCHERS || taskInfo.getState() == TaskStatus.END){
			log.info("根据业务ID：{},查询此任务已不存在/不是精准发券任务/已结束=====>>移除此任务！",activityId);
			quartzManagerService.delete(activityId, TaskJobService.PRECISION_VOUCHERS);
			return;
		}

		BaseResponse<CouponActivityDetailByActivityIdResponse> baseResponse = couponActivityQueryProvider.getByActivityId(new CouponActivityGetByActivityIdRequest(activityId));
		CouponActivityDetailByActivityIdResponse response = baseResponse.getContext();
		CouponActivityVO couponActivityVO = response.getCouponActivity();
		List<CouponActivityConfigVO> couponActivityConfigVOList = response.getCouponActivityConfigList();
		List<CouponInfoVO> couponInfoList = response.getCouponInfoList();
		if (Objects.isNull(couponActivityVO) || CollectionUtils.isEmpty(couponActivityConfigVOList)
				|| CollectionUtils.isEmpty(couponInfoList) || CouponActivityType.SPECIFY_COUPON != couponActivityVO.getCouponActivityType()
				|| DeleteFlag.YES == couponActivityVO.getDelFlag() || StringUtils.isBlank(couponActivityVO.getJoinLevel())){
			log.info("根据优惠券活动ID：{},未查询到活动信息/关联优惠券信息，或者此活动类型不是指定赠券/此活动已删除========>>更新任务状态&&移除此任务",activityId);
			//更新任务为已结束
			taskInfo.setEndTime(LocalDateTime.now());
			taskInfo.setState(TaskStatus.END);
			taskJobService.addTaskJob(taskInfo);
			//删除任务
			quartzManagerService.delete(activityId, TaskJobService.PRECISION_VOUCHERS);
			return;
		}

		log.info("根据优惠券活动ID：{},活动信息：{}，关联优惠券信息：{},优惠券信息：{}，精准发券流程开始",activityId,couponActivityVO,couponActivityConfigVOList,couponInfoList);

		//组装发券信息
		List<CouponInfoDTO> couponInfoDTOList = KsBeanUtil.convert(couponInfoList,CouponInfoDTO.class);
		final Map<String,CouponInfoDTO> couponInfoDTOMap = couponInfoDTOList.stream().collect(Collectors.toMap(CouponInfoDTO::getCouponId, Function.identity()));
		List<CouponActivityConfigAndCouponInfoDTO> configAndCouponInfoDTOS = couponActivityConfigVOList.stream().map(couponActivityConfigVO -> {
			CouponActivityConfigAndCouponInfoDTO dto = new CouponActivityConfigAndCouponInfoDTO();
			dto.setActivityConfigId(couponActivityConfigVO.getActivityConfigId());
			dto.setActivityId(couponActivityConfigVO.getActivityId());
			dto.setCouponId(couponActivityConfigVO.getCouponId());
			dto.setHasLeft(couponActivityConfigVO.getHasLeft());
			dto.setTotalCount(couponActivityConfigVO.getTotalCount());
			dto.setCouponInfoDTO(couponInfoDTOMap.get(couponActivityConfigVO.getCouponId()));
			return dto;
		}).collect(Collectors.toList());

		//处理精准发放优惠券
		handlePrecisionVouchers(couponActivityVO,configAndCouponInfoDTOS,taskInfo);
	}

	/**
	 * 处理精准发放优惠券
	 * @param couponActivityVO
	 * @param configAndCouponInfoDTOS
	 * @param taskInfo
	 */
	private void handlePrecisionVouchers(CouponActivityVO couponActivityVO ,List<CouponActivityConfigAndCouponInfoDTO> configAndCouponInfoDTOS,TaskInfo taskInfo ){
		String activityId = couponActivityVO.getActivityId();

		String joinLevel = couponActivityVO.getJoinLevel();
		MarketingJoinLevel marketingJoinLevel;
		if (joinLevel.equals(String.valueOf(MarketingJoinLevel.ALL_CUSTOMER.toValue())) || joinLevel.equals(String.valueOf(MarketingJoinLevel.SPECIFY_CUSTOMER.toValue())) || joinLevel.equals(String.valueOf(MarketingJoinLevel.ALL_LEVEL.toValue()))
		|| joinLevel.equals(String.valueOf(MarketingJoinLevel.ENTERPRISE_CUSTOMER.toValue())) || joinLevel.equals(String.valueOf(MarketingJoinLevel.PAID_CARD_CUSTOMER.toValue()))){
			marketingJoinLevel = MarketingJoinLevel.fromValue(Integer.valueOf(joinLevel));
		}else{
			marketingJoinLevel = MarketingJoinLevel.LEVEL_LIST;

		}
		log.info("根据优惠券活动ID：{},关联的客户等级：{}，发券详情信息：{},精准发券开始！",activityId,marketingJoinLevel,configAndCouponInfoDTOS);

		Integer pageNum = NumberUtils.INTEGER_ZERO;
		Integer pageSize = 1000;
		while (Boolean.TRUE){
			List<String> customerIds = new ArrayList<>();
			if (MarketingJoinLevel.SPECIFY_CUSTOMER.equals(marketingJoinLevel)) {
				//指定用户
				BaseResponse  result = couponCodeProvider.precisionVouchers(new CouponCodeBatchSendCouponRequest(configAndCouponInfoDTOS));
				if (ResultCode.SUCCESSFUL.equals(result.getCode())){
					log.info("根据优惠券活动ID：{},精准发券，指定用户，发券成功========>>更新任务状态&&移除此任务",activityId);
					modifyTaskInfo(taskInfo,activityId);
				}
				break;
			}
			if(MarketingJoinLevel.ENTERPRISE_CUSTOMER.equals(marketingJoinLevel)) {
				//企业会员
				EnterpriseCustomerPageRequest pageRequest = new EnterpriseCustomerPageRequest();
				pageRequest.setPageNum(pageNum);
				pageRequest.setPageSize(pageSize);
				pageRequest.setEnterpriseCheckState(EnterpriseCheckState.CHECKED);
				List<CustomerDetailForPageVO> detailResponseList = customerQueryProvider.pageForEnterpriseCustomer(pageRequest).getContext().getDetailResponseList();
				if(CollectionUtils.isNotEmpty(detailResponseList)) {
					customerIds = detailResponseList.stream().map(CustomerDetailForPageVO::getCustomerId).collect(Collectors.toList());
				}
			} else if(MarketingJoinLevel.PAID_CARD_CUSTOMER.equals(marketingJoinLevel)) {
				//付费会员
				PaidCustomerIdPageRequest request = new PaidCustomerIdPageRequest();
				request.setPageNum(pageNum);
				request.setPageSize(pageSize);
				customerIds = paidCardCustomerRelQueryProvider.listCustomerIdByPageable(request).getContext().getCustomerIds();
			} else if (MarketingJoinLevel.ALL_CUSTOMER.equals(marketingJoinLevel)) {
				//全平台
				CustomerListCustomerIdByPageableRequest request = new CustomerListCustomerIdByPageableRequest();
				request.setPageSize(pageSize);
				request.setPageNum(pageNum);
				customerIds = customerQueryProvider.listCustomerIdByPageable(request).getContext().getCustomerIds();
			} else if (MarketingJoinLevel.ALL_LEVEL.equals(marketingJoinLevel)) {
				//全店铺
				StoreCustomerRelaListCustomerIdByStoreIdRequest request = new StoreCustomerRelaListCustomerIdByStoreIdRequest();
				request.setStoreId(couponActivityVO.getStoreId());
				request.setPageSize(pageSize);
				request.setPageNum(pageNum);
				customerIds = storeCustomerQueryProvider.listCustomerIdByStoreId(request).getContext().getCustomerIds();
			} else if (MarketingJoinLevel.LEVEL_LIST.equals(marketingJoinLevel)) {
				DefaultFlag joinLevelType = couponActivityVO.getJoinLevelType();
				List<Long> storeLevelIds = Splitter.on(",").trimResults().splitToList(couponActivityVO.getJoinLevel()).stream().map(j ->Long.valueOf(j)).collect(Collectors.toList());
				if (DefaultFlag.NO == joinLevelType){
					StoreCustomerRelaListCustomerIdByStoreIdRequest request = new StoreCustomerRelaListCustomerIdByStoreIdRequest();
					request.setStoreId(couponActivityVO.getStoreId());
					request.setStoreLevelIds(storeLevelIds);
					request.setPageSize(pageSize);
					request.setPageNum(pageNum);
					customerIds = storeCustomerQueryProvider.listCustomerIdByStoreId(request).getContext().getCustomerIds();
				}else{
					CustomerListCustomerIdByPageableRequest request = new CustomerListCustomerIdByPageableRequest();
					request.setPageSize(pageSize);
					request.setPageNum(pageNum);
					request.setCustomerLevelIds(storeLevelIds);
					customerIds = customerQueryProvider.listCustomerIdByPageable(request).getContext().getCustomerIds();
				}
			}else{
				log.info("根据优惠券活动ID：{},发券类型：{}，发券类型不符合要求，请检查活动配置相关信息",activityId,marketingJoinLevel);
				break;
			}
			if(CollectionUtils.isEmpty(customerIds)){
				log.info("根据优惠券活动ID：{},发券类型：{}，已查询不到用户，发券成功========>>更新任务状态&&移除此任务",activityId,marketingJoinLevel);
				//更新任务为已结束
				modifyTaskInfo(taskInfo,activityId);
				break;
			}
			log.info("根据优惠券活动ID：{},发券类型：{}，发放指定用户ID集合详情信息：{}，开始发券！",activityId,marketingJoinLevel,customerIds);
			couponCodeProvider.precisionVouchers(new CouponCodeBatchSendCouponRequest(customerIds,configAndCouponInfoDTOS));
			pageNum++;
		}
	}

	/**
	 * 更新任务状态
	 * @param taskInfo
	 * @param activityId
	 */
	private void modifyTaskInfo(TaskInfo taskInfo,String activityId){
		//更新任务为已结束
		taskInfo.setEndTime(LocalDateTime.now());
		taskInfo.setState(TaskStatus.END);
		taskJobService.addTaskJob(taskInfo);
		//删除任务
		quartzManagerService.delete(activityId, TaskJobService.PRECISION_VOUCHERS);
	}
}