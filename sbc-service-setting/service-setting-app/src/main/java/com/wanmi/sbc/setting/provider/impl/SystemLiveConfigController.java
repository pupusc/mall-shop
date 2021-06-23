package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.setting.api.provider.SystemLiveConfigProvider;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemLiveStatusModifyRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import com.wanmi.sbc.common.base.BaseResponse;

import com.wanmi.sbc.setting.systemliveconfig.service.SystemLiveConfigService;

import javax.validation.Valid;

/**
 * <p>小程序直播设置保存服务接口实现</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@RestController
@Validated
public class SystemLiveConfigController implements SystemLiveConfigProvider {
	@Autowired
	private SystemLiveConfigService systemLiveConfigService;


	@Override
	public BaseResponse modifySystemLiveStatus(@Valid SystemLiveStatusModifyRequest systemLiveStatusModifyRequest) {

		systemLiveConfigService.modifySystemLiveStatus(systemLiveStatusModifyRequest);

		return BaseResponse.SUCCESSFUL();
	}
}

