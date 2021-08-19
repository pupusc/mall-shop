package com.wanmi.sbc.message.api.provider.pushcustomerenable;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableAddRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableModifyRequest;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableAddResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送通知开关保存服务Provider</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@FeignClient(value = "${application.message.name}", contextId = "PushCustomerEnableProvider")
public interface PushCustomerEnableProvider {

	/**
	 * 新增会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnableAddRequest 会员推送通知开关新增参数结构 {@link PushCustomerEnableAddRequest}
	 * @return 新增的会员推送通知开关信息 {@link PushCustomerEnableAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/add")
	BaseResponse<PushCustomerEnableAddResponse> add(@RequestBody @Valid PushCustomerEnableAddRequest pushCustomerEnableAddRequest);

	/**
	 * 修改会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnableModifyRequest 会员推送通知开关修改参数结构 {@link PushCustomerEnableModifyRequest}
	 * @return 修改的会员推送通知开关信息 {@link PushCustomerEnableModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/modify")
	BaseResponse<PushCustomerEnableModifyResponse> modify(@RequestBody @Valid PushCustomerEnableModifyRequest pushCustomerEnableModifyRequest);

	/**
	 * 单个删除会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnableDelByIdRequest 单个删除参数结构 {@link PushCustomerEnableDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PushCustomerEnableDelByIdRequest pushCustomerEnableDelByIdRequest);

}

