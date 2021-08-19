package com.wanmi.sbc.message.provider.impl.smssend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendQueryProvider;
import com.wanmi.sbc.message.api.request.smssend.SmsSendPageRequest;
import com.wanmi.sbc.message.api.request.smssend.SmsSendQueryRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendPageResponse;
import com.wanmi.sbc.message.api.request.smssend.SmsSendListRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendListResponse;
import com.wanmi.sbc.message.api.request.smssend.SmsSendByIdRequest;
import com.wanmi.sbc.message.api.response.smssend.SmsSendByIdResponse;
import com.wanmi.sbc.message.bean.vo.SmsSendVO;
import com.wanmi.sbc.message.smssend.service.SmsSendService;
import com.wanmi.sbc.message.smssend.model.root.SmsSend;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>短信发送查询服务接口实现</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@RestController
@Validated
public class SmsSendQueryController implements SmsSendQueryProvider {
	@Autowired
	private SmsSendService smsSendService;

	@Override
	public BaseResponse<SmsSendPageResponse> page(@RequestBody @Valid SmsSendPageRequest smsSendPageReq) {
		SmsSendQueryRequest queryReq = new SmsSendQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSendPageReq, queryReq);
		Page<SmsSend> smsSendPage = smsSendService.page(queryReq);
		Page<SmsSendVO> newPage = smsSendPage.map(entity -> smsSendService.wrapperVo(entity));
		MicroServicePage<SmsSendVO> microPage = new MicroServicePage<>(newPage, smsSendPageReq.getPageable());
		SmsSendPageResponse finalRes = new SmsSendPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SmsSendListResponse> list(@RequestBody @Valid SmsSendListRequest smsSendListReq) {
		SmsSendQueryRequest queryReq = new SmsSendQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSendListReq, queryReq);
		List<SmsSend> smsSendList = smsSendService.list(queryReq);
		List<SmsSendVO> newList = smsSendList.stream().map(entity -> smsSendService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsSendListResponse(newList));
	}

	@Override
	public BaseResponse<SmsSendByIdResponse> getById(@RequestBody @Valid SmsSendByIdRequest smsSendByIdRequest) {
		SmsSend smsSend = smsSendService.getById(smsSendByIdRequest.getId());
		return BaseResponse.success(new SmsSendByIdResponse(smsSendService.wrapperVo(smsSend)));
	}

}

