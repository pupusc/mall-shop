package com.wanmi.sbc.message.provider.impl.appmessage;

import com.wanmi.sbc.message.api.request.appmessage.*;
import com.wanmi.sbc.message.bean.enums.MessageType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.appmessage.AppMessageQueryProvider;
import com.wanmi.sbc.message.api.response.appmessage.AppMessagePageResponse;
import com.wanmi.sbc.message.api.response.appmessage.AppMessageListResponse;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import com.wanmi.sbc.message.appmessage.service.AppMessageService;
import com.wanmi.sbc.message.appmessage.model.root.AppMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>App站内信消息发送表查询服务接口实现</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@RestController
@Validated
public class AppMessageQueryController implements AppMessageQueryProvider {
	@Autowired
	private AppMessageService appMessageService;

	@Override
	public BaseResponse<AppMessagePageResponse> page(@RequestBody @Valid AppMessagePageRequest appMessagePageReq) {
		AppMessageQueryRequest queryReq = KsBeanUtil.convert(appMessagePageReq, AppMessageQueryRequest.class);
		Page<AppMessage> appMessagePage = appMessageService.page(queryReq);
		Page<AppMessageVO> newPage = appMessagePage.map(entity -> appMessageService.wrapperVo(entity));
		MicroServicePage<AppMessageVO> microPage = new MicroServicePage<>(newPage, appMessagePageReq.getPageable());
		int preferentialNum = appMessageService.getMessageCount(queryReq.getCustomerId(), MessageType.Preferential);
		int noticeNum = appMessageService.getMessageCount(queryReq.getCustomerId(), MessageType.Notice);
		AppMessagePageResponse finalRes = new AppMessagePageResponse(microPage, noticeNum, preferentialNum);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<AppMessageListResponse> list(@RequestBody @Valid AppMessageListRequest appMessageListReq) {
		AppMessageQueryRequest queryReq = KsBeanUtil.convert(appMessageListReq, AppMessageQueryRequest.class);
		List<AppMessage> appMessageList = appMessageService.list(queryReq);
		List<AppMessageVO> newList = appMessageList.stream().map(entity -> appMessageService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new AppMessageListResponse(newList));
	}


}

