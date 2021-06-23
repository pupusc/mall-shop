package com.wanmi.sbc.message.api.provider.smssenddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailPageRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailPageResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailListRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailListResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailByIdRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信发送查询服务Provider</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSendDetailQueryProvider")
public interface SmsSendDetailQueryProvider {

	/**
	 * 分页查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailPageReq 分页请求参数和筛选对象 {@link SmsSendDetailPageRequest}
	 * @return 短信发送分页列表信息 {@link SmsSendDetailPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/page")
	BaseResponse<SmsSendDetailPageResponse> page(@RequestBody @Valid SmsSendDetailPageRequest smsSendDetailPageReq);

	/**
	 * 列表查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailListReq 列表请求参数和筛选对象 {@link SmsSendDetailListRequest}
	 * @return 短信发送的列表信息 {@link SmsSendDetailListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/list")
	BaseResponse<SmsSendDetailListResponse> list(@RequestBody @Valid SmsSendDetailListRequest smsSendDetailListReq);

	/**
	 * 单个查询短信发送API
	 *
	 * @author zgl
	 * @param smsSendDetailByIdRequest 单个查询短信发送请求参数 {@link SmsSendDetailByIdRequest}
	 * @return 短信发送详情 {@link SmsSendDetailByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssenddetail/get-by-id")
	BaseResponse<SmsSendDetailByIdResponse> getById(@RequestBody @Valid SmsSendDetailByIdRequest smsSendDetailByIdRequest);

}

