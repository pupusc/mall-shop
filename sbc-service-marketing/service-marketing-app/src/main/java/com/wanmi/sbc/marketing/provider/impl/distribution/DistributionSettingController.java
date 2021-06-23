package com.wanmi.sbc.marketing.provider.impl.distribution;

import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingProvider;
import com.wanmi.sbc.marketing.api.request.distribution.*;
import com.wanmi.sbc.marketing.distribution.service.DistributionSettingService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import javax.validation.Valid;

/**
 * <p>分销设置保存服务接口实现</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@RestController
@Validated
public class DistributionSettingController implements DistributionSettingProvider {

	@Autowired
	private DistributionSettingService distributionSettingService;

	@Override
	public BaseResponse saveOpenFlag(@RequestBody @Valid DistributionOpenFlagSaveRequest request) {
		distributionSettingService.saveOpenFlag(request.getOpenFlag());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveBasic(@RequestBody @Valid DistributionBasicSettingSaveRequest request) {
		distributionSettingService.saveBasic(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveRecruit(@RequestBody @Valid DistributionRecruitSettingSaveRequest request) {
		distributionSettingService.saveRecruit(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveReward(@RequestBody @Valid DistributionRewardSettingSaveRequest request) {
		distributionSettingService.saveReward(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveMultistage(@RequestBody @Valid DistributionMultistageSettingSaveRequest request) {
		distributionSettingService.saveMultistage(request);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveStoreSetting(@RequestBody @Valid DistributionStoreSettingSaveRequest request) {
		distributionSettingService.saveStoreSetting(request);
		return BaseResponse.SUCCESSFUL();
	}

}

