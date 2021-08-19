package com.wanmi.sbc.message.provider.impl.smssetting;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssetting.SmsSettingSaveProvider;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingAddRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingAddResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingModifyRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingModifyResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingDelByIdRequest;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingDelByIdListRequest;
import com.wanmi.sbc.message.smssetting.service.SmsSettingService;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import javax.validation.Valid;

/**
 * <p>短信配置保存服务接口实现</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@RestController
@Validated
public class SmsSettingSaveController implements SmsSettingSaveProvider {
	@Autowired
	private SmsSettingService smsSettingService;

	@Override
	public BaseResponse<SmsSettingAddResponse> add(@RequestBody @Valid SmsSettingAddRequest smsSettingAddRequest) {
		SmsSetting smsSetting = new SmsSetting();
		KsBeanUtil.copyPropertiesThird(smsSettingAddRequest, smsSetting);
		return BaseResponse.success(new SmsSettingAddResponse(
				smsSettingService.wrapperVo(smsSettingService.add(smsSetting))));
	}

	@Override
	public BaseResponse<SmsSettingModifyResponse> modify(@RequestBody @Valid SmsSettingModifyRequest smsSettingModifyRequest) {
		SmsSetting smsSetting = new SmsSetting();
		KsBeanUtil.copyPropertiesThird(smsSettingModifyRequest, smsSetting);
		return BaseResponse.success(new SmsSettingModifyResponse(
				smsSettingService.wrapperVo(smsSettingService.modify(smsSetting))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid SmsSettingDelByIdRequest smsSettingDelByIdRequest) {
		smsSettingService.deleteById(smsSettingDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid SmsSettingDelByIdListRequest smsSettingDelByIdListRequest) {
		smsSettingService.deleteByIdList(smsSettingDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

