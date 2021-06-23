package com.wanmi.sbc.setting.provider.impl.businessconfig;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.businessconfig.BusinessConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigAddRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigAddResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigModifyResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigDelByIdListRequest;
import com.wanmi.sbc.setting.businessconfig.service.BusinessConfigService;
import com.wanmi.sbc.setting.businessconfig.model.root.BusinessConfig;
import javax.validation.Valid;

/**
 * <p>招商页设置保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@RestController
@Validated
public class BusinessConfigSaveController implements BusinessConfigSaveProvider {
	@Autowired
	private BusinessConfigService businessConfigService;

	@Override
	public BaseResponse<BusinessConfigAddResponse> add(@RequestBody @Valid BusinessConfigAddRequest businessConfigAddRequest) {
		BusinessConfig businessConfig = new BusinessConfig();
		KsBeanUtil.copyPropertiesThird(businessConfigAddRequest, businessConfig);
		return BaseResponse.success(new BusinessConfigAddResponse(
				businessConfigService.wrapperVo(businessConfigService.add(businessConfig))));
	}

	@Override
	public BaseResponse<BusinessConfigModifyResponse> modify(@RequestBody @Valid BusinessConfigModifyRequest businessConfigModifyRequest) {
		BusinessConfig businessConfig = new BusinessConfig();
		KsBeanUtil.copyPropertiesThird(businessConfigModifyRequest, businessConfig);
		return BaseResponse.success(new BusinessConfigModifyResponse(
				businessConfigService.wrapperVo(businessConfigService.modify(businessConfig))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid BusinessConfigDelByIdRequest businessConfigDelByIdRequest) {
		businessConfigService.deleteById(businessConfigDelByIdRequest.getBusinessConfigId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid BusinessConfigDelByIdListRequest businessConfigDelByIdListRequest) {
		businessConfigService.deleteByIdList(businessConfigDelByIdListRequest.getBusinessConfigIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

