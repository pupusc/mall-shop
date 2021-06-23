package com.wanmi.sbc.setting.provider.impl.push;

import com.wanmi.sbc.setting.api.provider.push.AppPushConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigAddRequest;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigAddResponse;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigModifyResponse;
import com.wanmi.sbc.setting.push.model.root.AppPushConfig;
import com.wanmi.sbc.setting.push.service.AppPushConfigService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import javax.validation.Valid;

/**
 * <p>消息推送保存服务接口实现</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@RestController
@Validated
public class AppPushConfigSaveController implements AppPushConfigSaveProvider {

	@Autowired
	private AppPushConfigService appPushConfigService;

	@Override
	public BaseResponse<AppPushConfigAddResponse> add(@RequestBody @Valid AppPushConfigAddRequest appPushConfigAddRequest) {
		AppPushConfig appPushConfig = new AppPushConfig();
		KsBeanUtil.copyPropertiesThird(appPushConfigAddRequest, appPushConfig);
		return BaseResponse.success(new AppPushConfigAddResponse(
				appPushConfigService.wrapperVo(appPushConfigService.add(appPushConfig))));
	}

	@Override
	public BaseResponse<AppPushConfigModifyResponse> modify(@RequestBody @Valid AppPushConfigModifyRequest appPushConfigModifyRequest) {
		AppPushConfig appPushConfig = new AppPushConfig();
		KsBeanUtil.copyPropertiesThird(appPushConfigModifyRequest, appPushConfig);
		return BaseResponse.success(new AppPushConfigModifyResponse(
				appPushConfigService.wrapperVo(appPushConfigService.modify(appPushConfig))));
	}

}

