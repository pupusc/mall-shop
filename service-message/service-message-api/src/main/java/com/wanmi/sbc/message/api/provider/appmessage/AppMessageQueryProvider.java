package com.wanmi.sbc.message.api.provider.appmessage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.appmessage.AppMessagePageRequest;
import com.wanmi.sbc.message.api.response.appmessage.AppMessagePageResponse;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageListRequest;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>App站内信消息发送表查询服务Provider</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@FeignClient(value = "${application.message.name}", contextId = "AppMessageQueryProvider")
public interface AppMessageQueryProvider {

	/**
	 * 分页查询App站内信消息发送表API
	 *
	 * @author xuyunpeng
	 * @param appMessagePageReq 分页请求参数和筛选对象 {@link AppMessagePageRequest}
	 * @return App站内信消息发送表分页列表信息 {@link AppMessagePageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/page")
	BaseResponse<AppMessagePageResponse> page(@RequestBody @Valid AppMessagePageRequest appMessagePageReq);

	/**
	 * 列表查询App站内信消息发送表API
	 *
	 * @author xuyunpeng
	 * @param appMessageListReq 列表请求参数和筛选对象 {@link AppMessageListRequest}
	 * @return App站内信消息发送表的列表信息 {@link AppMessageListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/appmessage/list")
	BaseResponse<AppMessageListResponse> list(@RequestBody @Valid AppMessageListRequest appMessageListReq);


}

