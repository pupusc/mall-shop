package com.wanmi.sbc.message.api.provider.appmessage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.appmessage.*;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageAddResponse;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>App站内信消息发送表保存服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@FeignClient(value = "${application.message.name}", contextId = "AppMessageProvider")
public interface AppMessageProvider {

	/**
	 * 新增App站内信消息发送表API
	 *
	 * @author xuyunpeng
	 * @param appMessageAddRequest App站内信消息发送表新增参数结构 {@link AppMessageAddRequest}
	 * @return 新增的App站内信消息发送表信息 {@link AppMessageAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/add")
	BaseResponse<AppMessageAddResponse> addBatch(@RequestBody @Valid AppMessageAddRequest appMessageAddRequest);

	/**
	 * 修改App站内信消息发送表API
	 *
	 * @author xuyunpeng
	 * @param appMessageModifyRequest App站内信消息发送表修改参数结构 {@link AppMessageModifyRequest}
	 * @return 修改的App站内信消息发送表信息 {@link AppMessageModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/modify")
	BaseResponse<AppMessageModifyResponse> modify(@RequestBody @Valid AppMessageModifyRequest appMessageModifyRequest);

	/**
	 * 单个删除App站内信消息发送表API
	 *
	 * @author xuyunpeng
	 * @param appMessageDelByIdRequest 单个删除参数结构 {@link AppMessageDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid AppMessageDelByIdRequest appMessageDelByIdRequest);

	/**
	 * 将消息置为已读API
	 *
	 * @author xuyunpeng
	 * @param request 单个查询App站内信消息发送表请求参数 {@link AppMessageSetReadRequest}
	 * @return
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/set-message-read")
	BaseResponse setMessageRead(@RequestBody @Valid AppMessageSetReadRequest request);

	/**
	 * 将消息全部置为已读API
	 *
	 * @author xuyunpeng
	 * @param request 单个查询App站内信消息发送表请求参数 {@link AppMessageSetReadRequest}
	 * @return
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/set-message-all-read")
	BaseResponse setMessageAllRead(@RequestBody @Valid AppMessageSetReadRequest request);

}

