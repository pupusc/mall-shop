package com.wanmi.sbc.message.provider.impl.pushcustomerenable;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushcustomerenable.PushCustomerEnableProvider;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableAddRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableModifyRequest;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableAddResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableModifyResponse;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import com.wanmi.sbc.message.pushcustomerenable.service.PushCustomerEnableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>会员推送通知开关保存服务接口实现</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@RestController
@Validated
public class PushCustomerEnableController implements PushCustomerEnableProvider {
	@Autowired
	private PushCustomerEnableService pushCustomerEnableService;

	@Override
	public BaseResponse<PushCustomerEnableAddResponse> add(@RequestBody @Valid PushCustomerEnableAddRequest pushCustomerEnableAddRequest) {
		PushCustomerEnable pushCustomerEnable = KsBeanUtil.convert(pushCustomerEnableAddRequest, PushCustomerEnable.class);
		return BaseResponse.success(new PushCustomerEnableAddResponse(
				pushCustomerEnableService.wrapperVo(pushCustomerEnableService.add(pushCustomerEnable))));
	}

	@Override
	public BaseResponse<PushCustomerEnableModifyResponse> modify(@RequestBody @Valid PushCustomerEnableModifyRequest pushCustomerEnableModifyRequest) {
		PushCustomerEnable pushCustomerEnable = KsBeanUtil.convert(pushCustomerEnableModifyRequest, PushCustomerEnable.class);
		return BaseResponse.success(new PushCustomerEnableModifyResponse(
				pushCustomerEnableService.wrapperVo(pushCustomerEnableService.modify(pushCustomerEnable))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PushCustomerEnableDelByIdRequest pushCustomerEnableDelByIdRequest) {
		pushCustomerEnableService.deleteById(pushCustomerEnableDelByIdRequest.getCustomerId());
		return BaseResponse.SUCCESSFUL();
	}
}

