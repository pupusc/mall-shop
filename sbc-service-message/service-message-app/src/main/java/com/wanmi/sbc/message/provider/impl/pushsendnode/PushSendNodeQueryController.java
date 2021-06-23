package com.wanmi.sbc.message.provider.impl.pushsendnode;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeQueryProvider;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeByIdRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeListRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodePageRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeQueryRequest;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeByIdResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodeListResponse;
import com.wanmi.sbc.message.api.response.pushsendnode.PushSendNodePageResponse;
import com.wanmi.sbc.message.bean.vo.PushSendNodeVO;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import com.wanmi.sbc.message.pushsendnode.service.PushSendNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员推送通知节点查询服务接口实现</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@RestController
@Validated
public class PushSendNodeQueryController implements PushSendNodeQueryProvider {
	@Autowired
	private PushSendNodeService pushSendNodeService;

	@Override
	public BaseResponse<PushSendNodePageResponse> page(@RequestBody @Valid PushSendNodePageRequest pushSendNodePageReq) {
		PushSendNodeQueryRequest queryReq = KsBeanUtil.convert(pushSendNodePageReq, PushSendNodeQueryRequest.class);
		Page<PushSendNode> pushSendNodePage = pushSendNodeService.page(queryReq);
		Page<PushSendNodeVO> newPage = pushSendNodePage.map(entity -> pushSendNodeService.wrapperVo(entity));
		MicroServicePage<PushSendNodeVO> microPage = new MicroServicePage<>(newPage, pushSendNodePageReq.getPageable());
		PushSendNodePageResponse finalRes = new PushSendNodePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PushSendNodeListResponse> list(@RequestBody @Valid PushSendNodeListRequest pushSendNodeListReq) {
		PushSendNodeQueryRequest queryReq = KsBeanUtil.convert(pushSendNodeListReq, PushSendNodeQueryRequest.class);
		List<PushSendNode> pushSendNodeList = pushSendNodeService.list(queryReq);
		List<PushSendNodeVO> newList = pushSendNodeList.stream().map(entity -> pushSendNodeService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PushSendNodeListResponse(newList));
	}

	@Override
	public BaseResponse<PushSendNodeByIdResponse> getById(@RequestBody @Valid PushSendNodeByIdRequest pushSendNodeByIdRequest) {
		PushSendNode pushSendNode =
		pushSendNodeService.getOne(pushSendNodeByIdRequest.getId());
		return BaseResponse.success(new PushSendNodeByIdResponse(pushSendNodeService.wrapperVo(pushSendNode)));
	}

}

