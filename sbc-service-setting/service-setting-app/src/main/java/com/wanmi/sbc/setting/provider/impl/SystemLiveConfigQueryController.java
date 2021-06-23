package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.setting.api.provider.SystemLiveConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemLiveOpenResponse;
import com.wanmi.sbc.setting.systemliveconfig.model.root.SystemLiveConfig;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import com.wanmi.sbc.setting.systemliveconfig.service.SystemLiveConfigService;

/**
 * <p>小程序直播设置查询服务接口实现</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@RestController
@Validated
public class SystemLiveConfigQueryController implements SystemLiveConfigQueryProvider {
	@Autowired
	private SystemLiveConfigService systemLiveConfigService;


	@Override
	public BaseResponse<SystemLiveOpenResponse> isLiveOpen() {
		SystemLiveOpenResponse response = new SystemLiveOpenResponse();
		SystemLiveConfig config = systemLiveConfigService.querySystemLiveConfig();
		response.setOpen(Constants.yes.equals(config.getStatus().toValue()));

		return BaseResponse.success(response);
	}
}

