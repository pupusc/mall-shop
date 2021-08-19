package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.SystemPointsOpenResponse;
import com.wanmi.sbc.setting.systempointsconfig.model.root.SystemPointsConfig;
import com.wanmi.sbc.setting.systempointsconfig.service.SystemPointsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>积分设置查询服务接口实现</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@RestController
@Validated
public class SystemPointsConfigQueryController implements SystemPointsConfigQueryProvider {
	@Autowired
	private SystemPointsConfigService systemPointsConfigService;


	@Override
	public BaseResponse<SystemPointsConfigQueryResponse> querySystemPointsConfig() {
		SystemPointsConfigQueryResponse response = new SystemPointsConfigQueryResponse();
		SystemPointsConfig config = systemPointsConfigService.querySystemPointsConfig();
		KsBeanUtil.copyPropertiesThird(config, response);
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<SystemPointsOpenResponse> isPointsOpen() {
		SystemPointsOpenResponse response = new SystemPointsOpenResponse();
		SystemPointsConfig config = systemPointsConfigService.querySystemPointsConfig();
		response.setOpen(Constants.yes.equals(config.getStatus().toValue()));

		return BaseResponse.success(response);
	}
}

