package com.wanmi.sbc.message.api.provider.pushsend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushsend.PushSendByIdRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendListRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendPageRequest;
import com.wanmi.sbc.message.api.response.pushsend.PushSendByIdResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendListResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送信息查询服务Provider</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@FeignClient(value = "${application.message.name}", contextId = "PushSendQueryProvider")
public interface PushSendQueryProvider {

	/**
	 * 分页查询会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendPageReq 分页请求参数和筛选对象 {@link PushSendPageRequest}
	 * @return 会员推送信息分页列表信息 {@link PushSendPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/page")
	BaseResponse<PushSendPageResponse> page(@RequestBody @Valid PushSendPageRequest pushSendPageReq);

	/**
	 * 列表查询会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendListReq 列表请求参数和筛选对象 {@link PushSendListRequest}
	 * @return 会员推送信息的列表信息 {@link PushSendListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/list")
	BaseResponse<PushSendListResponse> list(@RequestBody @Valid PushSendListRequest pushSendListReq);

	/**
	 * 单个查询会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendByIdRequest 单个查询会员推送信息请求参数 {@link PushSendByIdRequest}
	 * @return 会员推送信息详情 {@link PushSendByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/get-by-id")
	BaseResponse<PushSendByIdResponse> getById(@RequestBody @Valid PushSendByIdRequest pushSendByIdRequest);

}

