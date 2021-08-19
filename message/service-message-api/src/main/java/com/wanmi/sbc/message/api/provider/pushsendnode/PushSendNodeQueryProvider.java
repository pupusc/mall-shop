package com.wanmi.sbc.message.api.provider.pushsendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeByIdRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeListRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodePageRequest;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeByIdResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeListResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送通知节点查询服务Provider</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@FeignClient(value = "${application.message.name}", contextId = "PushSendNodeQueryProvider")
public interface PushSendNodeQueryProvider {

	/**
	 * 分页查询会员推送通知节点API
	 *
	 * @author Bob
	 * @param pushSendNodePageReq 分页请求参数和筛选对象 {@link PushSendNodePageRequest}
	 * @return 会员推送通知节点分页列表信息 {@link PushSendNodePageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/page")
	BaseResponse<PushSendNodePageResponse> page(@RequestBody @Valid PushSendNodePageRequest pushSendNodePageReq);

	/**
	 * 列表查询会员推送通知节点API
	 *
	 * @author Bob
	 * @param pushSendNodeListReq 列表请求参数和筛选对象 {@link PushSendNodeListRequest}
	 * @return 会员推送通知节点的列表信息 {@link PushSendNodeListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/list")
	BaseResponse<PushSendNodeListResponse> list(@RequestBody @Valid PushSendNodeListRequest pushSendNodeListReq);

	/**
	 * 单个查询会员推送通知节点API
	 *
	 * @author Bob
	 * @param pushSendNodeByIdRequest 单个查询会员推送通知节点请求参数 {@link PushSendNodeByIdRequest}
	 * @return 会员推送通知节点详情 {@link PushSendNodeByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/get-by-id")
	BaseResponse<PushSendNodeByIdResponse> getById(@RequestBody @Valid PushSendNodeByIdRequest pushSendNodeByIdRequest);

}

