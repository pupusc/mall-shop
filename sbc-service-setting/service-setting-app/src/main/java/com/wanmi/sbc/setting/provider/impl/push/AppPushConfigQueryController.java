package com.wanmi.sbc.setting.provider.impl.push;

import com.wanmi.sbc.setting.api.provider.push.AppPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigDetailResponse;
import com.wanmi.sbc.setting.push.model.root.AppPushConfig;
import com.wanmi.sbc.setting.push.service.AppPushConfigService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import javax.validation.Valid;

/**
 * <p>消息推送查询服务接口实现</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@RestController
@Validated
public class AppPushConfigQueryController implements AppPushConfigQueryProvider {

    @Autowired
	private AppPushConfigService appPushConfigService;

	@Override
	public BaseResponse<AppPushConfigByIdResponse> getById(@RequestBody @Valid AppPushConfigByIdRequest appPushConfigByIdRequest) {
		AppPushConfig appPushConfig = appPushConfigService.getById(appPushConfigByIdRequest.getAppPushId());
		return BaseResponse.success(new AppPushConfigByIdResponse(appPushConfigService.wrapperVo(appPushConfig)));
	}

	/**
	 * 查询友盟推送API
	 *
	 * @return 消息推送详情
	 * @author chenyufei
	 */
	@Override
	public BaseResponse<AppPushConfigDetailResponse> detail() {
		AppPushConfig appPushConfig = appPushConfigService.getById(1L);
		return BaseResponse.success(new AppPushConfigDetailResponse(appPushConfigService.wrapperVo(appPushConfig)));
	}

}

