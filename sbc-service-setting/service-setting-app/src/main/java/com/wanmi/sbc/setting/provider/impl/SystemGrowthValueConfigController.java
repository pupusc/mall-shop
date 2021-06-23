package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigProvider;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.growthValue.service.SystemGrowthValueConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>系统成长值设置保存服务接口实现</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@RestController
@Validated
public class SystemGrowthValueConfigController implements SystemGrowthValueConfigProvider {
	@Autowired
	private SystemGrowthValueConfigService systemGrowthValueConfigService;

	@Override
	public BaseResponse modifySystemGrowthValueConfig(@RequestBody @Valid SystemGrowthValueConfigModifyRequest systemGrowthValueConfigModifyRequest) {
		systemGrowthValueConfigService.modifySystemGrowthValueConfig(systemGrowthValueConfigModifyRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modifySystemGrowthValueStatus(@RequestBody @Valid SystemGrowthValueStatusModifyRequest systemGrowthValueStatusModifyRequest) {
		systemGrowthValueConfigService.modifySystemGrowthValueStatus(systemGrowthValueStatusModifyRequest);
		return BaseResponse.SUCCESSFUL();
	}

}

