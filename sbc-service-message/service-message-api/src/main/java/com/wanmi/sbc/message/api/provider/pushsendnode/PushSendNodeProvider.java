package com.wanmi.sbc.message.api.provider.pushsendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeModifyRequest;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送通知节点保存服务Provider</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@FeignClient(value = "${application.message.name}", contextId = "PushSendNodeProvider")
public interface PushSendNodeProvider {

	/**
	 * 修改会员推送通知节点API
	 *
	 * @author Bob
	 * @param pushSendNodeModifyRequest 会员推送通知节点修改参数结构 {@link PushSendNodeModifyRequest}
	 * @return 修改的会员推送通知节点信息 {@link PushSendNodeModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/modify")
	BaseResponse<PushSendNodeModifyResponse> modify(@RequestBody @Valid PushSendNodeModifyRequest pushSendNodeModifyRequest);

	/**
	 * 设置节点开关API
	 *
	 * @author Bob
	 * @param pushSendNodeModifyRequest 会员推送通知节点修改参数结构 {@link PushSendNodeModifyRequest}
	 * @return 修改的会员推送通知节点信息 {@link PushSendNodeModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/enabled")
	BaseResponse enabled(@RequestBody @Valid PushSendNodeModifyRequest pushSendNodeModifyRequest);

	/**
	 * 通知节点打开数等数据统计
	 *
	 * @author Bob
	 */
	@PostMapping("/sms/${application.sms.version}/pushsendnode/nodeTask")
	BaseResponse nodeTask();



}

