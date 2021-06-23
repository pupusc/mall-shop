package com.wanmi.sbc.message.provider.impl.pushsendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeProvider;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeModifyRequest;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeModifyResponse;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import com.wanmi.sbc.message.pushsendnode.service.PushSendNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>会员推送通知节点保存服务接口实现</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@RestController
@Validated
public class PushSendNodeController implements PushSendNodeProvider {
	@Autowired
	private PushSendNodeService pushSendNodeService;

	@Override
	public BaseResponse<PushSendNodeModifyResponse> modify(@RequestBody @Valid PushSendNodeModifyRequest pushSendNodeModifyRequest) {
		PushSendNode pushSendNode = KsBeanUtil.convert(pushSendNodeModifyRequest, PushSendNode.class);
		return BaseResponse.success(new PushSendNodeModifyResponse(
				pushSendNodeService.wrapperVo(pushSendNodeService.modify(pushSendNode))));
	}

	/**
	 * 设置节点开关API
	 *
	 * @param pushSendNodeModifyRequest 会员推送通知节点修改参数结构 {@link PushSendNodeModifyRequest}
	 * @return 修改的会员推送通知节点信息 {@link PushSendNodeModifyResponse}
	 * @author Bob
	 */
	@Override
	public BaseResponse enabled(@RequestBody @Valid PushSendNodeModifyRequest pushSendNodeModifyRequest) {
		PushSendNode pushSendNode = KsBeanUtil.convert(pushSendNodeModifyRequest, PushSendNode.class);
		pushSendNodeService.enabled(pushSendNode);
		return BaseResponse.SUCCESSFUL();
	}

	/**
	 * 通知节点打开数等数据统计
	 * @author Bob
	 */
	@Override
	public BaseResponse nodeTask() {
		pushSendNodeService.taskForUpdate();
		return BaseResponse.SUCCESSFUL();
	}
}

