package com.wanmi.sbc.message.api.provider.smsbase;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smsbase.SmsSendRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>短信发送服务Provider</p>
 * @author dyt
 * @date 2019-12-03 15:36:05
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsBaseProvider")
public interface SmsBaseProvider {

	/**
	 * 短信发送API
	 *
	 * @author dyt
	 * @param request 请求发送结构 {@link SmsSendRequest}
	 * @return 结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/sms/send")
	BaseResponse send(@RequestBody @Valid SmsSendRequest request);

}

