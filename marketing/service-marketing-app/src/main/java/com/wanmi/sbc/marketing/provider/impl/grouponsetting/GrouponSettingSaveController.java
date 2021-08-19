package com.wanmi.sbc.marketing.provider.impl.grouponsetting;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponsetting.GrouponSettingSaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingAddRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingAddResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingModifyRequest;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingModifyResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingDelByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponAuditFlagSaveRequest;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponPosterSaveRequest;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponRuleSaveRequest;
import com.wanmi.sbc.marketing.grouponsetting.service.GrouponSettingService;
import com.wanmi.sbc.marketing.grouponsetting.model.root.GrouponSetting;
import javax.validation.Valid;

/**
 * <p>拼团活动信息表保存服务接口实现</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@RestController
@Validated
public class GrouponSettingSaveController implements GrouponSettingSaveProvider {
	@Autowired
	private GrouponSettingService grouponSettingService;

	@Override
	public BaseResponse<GrouponSettingAddResponse> add(@RequestBody @Valid GrouponSettingAddRequest grouponSettingAddRequest) {
		GrouponSetting grouponSetting = new GrouponSetting();
		KsBeanUtil.copyPropertiesThird(grouponSettingAddRequest, grouponSetting);
		return BaseResponse.success(new GrouponSettingAddResponse(
				grouponSettingService.wrapperVo(grouponSettingService.add(grouponSetting))));
	}

	@Override
	public BaseResponse<GrouponSettingModifyResponse> modify(@RequestBody @Valid GrouponSettingModifyRequest grouponSettingModifyRequest) {
		GrouponSetting grouponSetting = new GrouponSetting();
		KsBeanUtil.copyPropertiesThird(grouponSettingModifyRequest, grouponSetting);
		return BaseResponse.success(new GrouponSettingModifyResponse(
				grouponSettingService.wrapperVo(grouponSettingService.modify(grouponSetting))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid GrouponSettingDelByIdRequest grouponSettingDelByIdRequest) {
		grouponSettingService.deleteById(grouponSettingDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}


	@Override
	public BaseResponse saveAudit(@RequestBody @Valid GrouponAuditFlagSaveRequest grouponAuditFlagSaveRequest) {
		grouponSettingService.saveAudit(grouponAuditFlagSaveRequest.getAuditFlag());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse savePoster(@RequestBody @Valid GrouponPosterSaveRequest grouponPosterSaveRequest) {
		grouponSettingService.savePoster(grouponPosterSaveRequest.getPoster());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse saveRule(@RequestBody @Valid GrouponRuleSaveRequest grouponRuleSaveRequest) {
		grouponSettingService.saveRule(grouponRuleSaveRequest.getRule());
		return BaseResponse.SUCCESSFUL();
	}
}

