package com.wanmi.sbc.setting.provider.impl.baseconfig;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigAddRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigAddResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigModifyResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigDelByIdListRequest;
import com.wanmi.sbc.setting.baseconfig.service.BaseConfigService;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import javax.validation.Valid;

/**
 * <p>基本设置保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@RestController
@Validated
public class BaseConfigSaveController implements BaseConfigSaveProvider {
	@Autowired
	private BaseConfigService baseConfigService;

	@Override
	public BaseResponse<BaseConfigAddResponse> add(@RequestBody @Valid BaseConfigAddRequest baseConfigAddRequest) {
		BaseConfig baseConfig = new BaseConfig();
		KsBeanUtil.copyPropertiesThird(baseConfigAddRequest, baseConfig);
		return BaseResponse.success(new BaseConfigAddResponse(
				baseConfigService.wrapperVo(baseConfigService.add(baseConfig))));
	}

	@Override
	public BaseResponse<BaseConfigModifyResponse> modify(@RequestBody @Valid BaseConfigModifyRequest baseConfigModifyRequest) {
		BaseConfig baseConfig = new BaseConfig();
		KsBeanUtil.copyPropertiesThird(baseConfigModifyRequest, baseConfig);
		return BaseResponse.success(new BaseConfigModifyResponse(
				baseConfigService.wrapperVo(baseConfigService.modify(baseConfig))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid BaseConfigDelByIdRequest baseConfigDelByIdRequest) {
		baseConfigService.deleteById(baseConfigDelByIdRequest.getBaseConfigId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid BaseConfigDelByIdListRequest baseConfigDelByIdListRequest) {
		baseConfigService.deleteByIdList(baseConfigDelByIdListRequest.getBaseConfigIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

