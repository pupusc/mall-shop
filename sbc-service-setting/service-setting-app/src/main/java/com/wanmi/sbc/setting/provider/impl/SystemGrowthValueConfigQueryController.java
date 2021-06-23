package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueOpenResponse;
import com.wanmi.sbc.setting.growthValue.model.root.SystemGrowthValueConfig;
import com.wanmi.sbc.setting.growthValue.service.SystemGrowthValueConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>系统成长值设置查询服务接口实现</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@RestController
@Validated
public class SystemGrowthValueConfigQueryController implements SystemGrowthValueConfigQueryProvider {
	@Autowired
	private SystemGrowthValueConfigService systemGrowthValueConfigService;

	@Override
	public BaseResponse<SystemGrowthValueConfigQueryResponse> querySystemGrowthValueConfig() {
		SystemGrowthValueConfigQueryResponse response = new SystemGrowthValueConfigQueryResponse();
		SystemGrowthValueConfig config = systemGrowthValueConfigService.querySystemGrowthValueConfig();
		KsBeanUtil.copyPropertiesThird(config, response);
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<SystemGrowthValueOpenResponse> isGrowthValueOpen() {
		SystemGrowthValueOpenResponse response = new SystemGrowthValueOpenResponse();
		SystemGrowthValueConfig config = systemGrowthValueConfigService.querySystemGrowthValueConfig();
		response.setOpen(Constants.yes.equals(config.getStatus().toValue()));

		return BaseResponse.success(response);
	}

}

