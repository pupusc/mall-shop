package com.wanmi.sbc.marketing.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerSimByIdRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerSimByIdResponse;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionSettingByDistributorIdRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingListByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.response.distribution.*;
import com.wanmi.sbc.marketing.distribution.service.DistributionSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>分销设置查询服务接口实现</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@RestController
@Validated
public class DistributionSettingQueryController implements DistributionSettingQueryProvider {

	@Autowired
	private DistributionSettingService distributionSettingService;

	@Autowired
	private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

	@Override
	public BaseResponse<DistributionSettingGetResponse> getSetting() {
		return BaseResponse.success(distributionSettingService.querySetting());
	}

	@Override
	public BaseResponse<Boolean> getDistributionGoodsSwitch() {
		return BaseResponse.success(distributionSettingService.queryDistributionGoodsSwitch());
	}

	@Override
	public BaseResponse<DistributionStoreSettingGetByStoreIdResponse> getStoreSettingByStoreId(
			@RequestBody @Valid DistributionStoreSettingGetByStoreIdRequest request) {
		return BaseResponse.success(distributionSettingService.queryStoreSetting(request.getStoreId()));
	}

	@Override
	public BaseResponse<DistributionStoreSettingListByStoreIdsResponse> listByStoreIds(
			@RequestBody @Valid DistributionStoreSettingListByStoreIdsRequest request) {
		return BaseResponse.success(distributionSettingService.findByStoreIdIn(request.getStoreIds()));
	}

	@Override
	public BaseResponse<DistributionSimSettingResponse> getSimSetting(@RequestBody @Valid DistributionSettingByDistributorIdRequest request) {
        DistributionSimSettingResponse response = distributionSettingService.querySimSetting();
        DistributionCustomerSimByIdResponse distributionResponse = distributionCustomerQueryProvider.getSimInfoById
                (new DistributionCustomerSimByIdRequest(request.getInviteeId())).getContext();
        response.setDistributionCustomerSimVO(distributionResponse.getDistributionCustomerSimVO());
		return BaseResponse.success(response);
	}

	/**
	 * 获取分销设置
	 *
	 * @return
	 */
	@Override
	public BaseResponse<DistributionSimSettingResponse> findOneSetting() {
		return BaseResponse.success(distributionSettingService.querySimSetting());
	}
}

