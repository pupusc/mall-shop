package com.wanmi.sbc.customer.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewPageRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerAddRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByInviteCodeRequest;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerByInviteCodeResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.marketing.bean.enums.RegisterLimitType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 会员
 * Created by Daiyitian on 2017/4/19.
 */
@Slf4j
@Service
public class LoginBaseService {

	@Autowired
	private DistributionCacheService distributionCacheService;

	@Autowired
	private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

	@Autowired
	private DistributionCustomerSaveProvider distributionCustomerSaveProvider;

	@Autowired
	private DistributionInviteNewQueryProvider distributionInviteNewQueryProvider;

	/**
	 * 验证邀请ID、邀请码
	 *
	 * @param inviteeId
	 * @param inviteCode
	 * @return
	 */
	public DistributionCustomerVO checkInviteIdAndInviteCode(String inviteeId, String inviteCode) {
		DistributionCustomerVO distributionCustomerVO = new DistributionCustomerVO();
		RegisterLimitType registerLimitType = distributionCacheService.getRegisterLimitType();
		DefaultFlag defaultFlag = distributionCacheService.queryOpenFlag();

		if (defaultFlag == DefaultFlag.YES && registerLimitType == RegisterLimitType.INVITE && StringUtils.isBlank(inviteCode) && StringUtils.isBlank(inviteeId)) {
			throw new SbcRuntimeException(SiteResultCode.ERROR_010105);
		}

		if (StringUtils.isNotBlank(inviteeId)) {
			DistributionInviteNewPageRequest inviteNewPageRequest = new DistributionInviteNewPageRequest();
			inviteNewPageRequest.setInvitedNewCustomerId(inviteeId);
			DistributionInviteNewPageResponse inviteNewPage =
					distributionInviteNewQueryProvider.findDistributionInviteNewRecord(inviteNewPageRequest).getContext();
			distributionCustomerVO.setCustomerId(inviteeId);
			if (inviteNewPage.getTotal() == 0 || CollectionUtils.isEmpty(inviteNewPage.getRecordList())) {
				return distributionCustomerVO;
			}
			String customerId = inviteNewPage.getRecordList().get(0).getRequestCustomerId();
			distributionCustomerVO.setInviteCustomerIds(customerId);
			return distributionCustomerVO;
		} else {
			if (StringUtils.isNotBlank(inviteCode)) {
				BaseResponse<DistributionCustomerByInviteCodeResponse> baseResponse = distributionCustomerQueryProvider.getByInviteCode(new DistributionCustomerByInviteCodeRequest(inviteCode));
				DistributionCustomerByInviteCodeResponse response = baseResponse.getContext();
				distributionCustomerVO = response.getDistributionCustomerVO();
				if (Objects.isNull(distributionCustomerVO)) {
					throw new SbcRuntimeException(SiteResultCode.ERROR_010106);
				}
				return distributionCustomerVO;
			}
		}
		return distributionCustomerVO;
	}

	/**
	 * 新增分销员
	 * @param customerId
	 * @param customerAccount
	 * @param customerName
	 */
	public void addDistributionCustomer(String customerId, String customerAccount, String customerName,String inviteCustomerIds) {
		DistributionCustomerAddRequest request = new DistributionCustomerAddRequest();
		request.setCustomerId(customerId);
		request.setCustomerAccount(customerAccount);
		request.setCustomerName(StringUtils.isBlank(customerName) ? customerAccount : customerName);
		request.setCreateTime(LocalDateTime.now());
		request.setInviteCount(NumberUtils.INTEGER_ZERO);
		// 邀新奖励
		request.setRewardCash(BigDecimal.ZERO);
		// 未入账邀新奖励
		request.setRewardCashNotRecorded(BigDecimal.ZERO);
		request.setInviteCustomerIds(inviteCustomerIds);
		BaseResponse<DistributionCustomerAddResponse> customerAddRespons = distributionCustomerSaveProvider.add(request);
		if (Objects.nonNull(customerAddRespons) && Objects.nonNull(customerAddRespons.getContext())
				&& Objects.nonNull(customerAddRespons.getContext().getDistributionCustomerVO())) {
			log.info("新增分销员信息成功");
		} else {
			log.error("新增分销员信息失败");
		}
	}


}
