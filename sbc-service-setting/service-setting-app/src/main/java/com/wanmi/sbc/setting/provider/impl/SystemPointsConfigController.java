package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigProvider;
import com.wanmi.sbc.setting.api.request.SystemPointsConfigModifyRequest;
import com.wanmi.sbc.setting.systempointsconfig.service.SystemPointsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>积分设置保存服务接口实现</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@RestController
@Validated
public class SystemPointsConfigController implements SystemPointsConfigProvider {
	@Autowired
	private SystemPointsConfigService systemPointsConfigService;


	@Override
	public BaseResponse modifySystemPointsConfig(@RequestBody @Valid SystemPointsConfigModifyRequest systemPointsConfigModifyRequest) {
		systemPointsConfigService.modifySystemPointsConfig(systemPointsConfigModifyRequest);
		return BaseResponse.SUCCESSFUL();
	}
}

