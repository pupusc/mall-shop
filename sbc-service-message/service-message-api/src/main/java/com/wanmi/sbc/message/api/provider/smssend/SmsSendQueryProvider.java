package com.wanmi.sbc.message.api.provider.smssend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssend.SmsSendPageRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendPageResponse;
import com.wanmi.sbc.message.api.request.smssend.SmsSendListRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendListResponse;
import com.wanmi.sbc.message.api.request.smssend.SmsSendByIdRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信发送查询服务Provider</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@FeignClient(value="${application.message.name}",contextId = "SmsSendQueryProvider")
public interface SmsSendQueryProvider {

	/**
	 * 分页查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendPageReq 分页请求参数和筛选对象 {@link SmsSendPageRequest}
	 * @return 短信发送分页列表信息 {@link SmsSendPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssend/page")
	BaseResponse<SmsSendPageResponse> page(@RequestBody @Valid SmsSendPageRequest smsSendPageReq);

	/**
	 * 列表查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendListReq 列表请求参数和筛选对象 {@link SmsSendListRequest}
	 * @return 短信发送的列表信息 {@link SmsSendListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssend/list")
	BaseResponse<SmsSendListResponse> list(@RequestBody @Valid SmsSendListRequest smsSendListReq);

	/**
	 * 单个查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendByIdRequest 单个查询短信发送请求参数 {@link SmsSendByIdRequest}
	 * @return 短信发送详情 {@link SmsSendByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssend/get-by-id")
	BaseResponse<SmsSendByIdResponse> getById(@RequestBody @Valid SmsSendByIdRequest smsSendByIdRequest);

}

