package com.wanmi.sbc.message.provider.impl.smssenddetail;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.smssenddetail.SmsSendDetailQueryProvider;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailPageRequest;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailQueryRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailPageResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailListRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailListResponse;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailByIdRequest;
import com.wanmi.sbc.message.api.response.smssenddetail.SmsSendDetailByIdResponse;
import com.wanmi.sbc.message.bean.vo.SmsSendDetailVO;
import com.wanmi.sbc.message.smssenddetail.service.SmsSendDetailService;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>短信发送查询服务接口实现</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@RestController
@Validated
public class SmsSendDetailQueryController implements SmsSendDetailQueryProvider {
	@Autowired
	private SmsSendDetailService smsSendDetailService;

	@Override
	public BaseResponse<SmsSendDetailPageResponse> page(@RequestBody @Valid SmsSendDetailPageRequest smsSendDetailPageReq) {
		SmsSendDetailQueryRequest queryReq = new SmsSendDetailQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSendDetailPageReq, queryReq);
		Page<SmsSendDetail> smsSendDetailPage = smsSendDetailService.page(queryReq);
		Page<SmsSendDetailVO> newPage = smsSendDetailPage.map(entity -> smsSendDetailService.wrapperVo(entity));
		MicroServicePage<SmsSendDetailVO> microPage = new MicroServicePage<>(newPage, smsSendDetailPageReq.getPageable());
		SmsSendDetailPageResponse finalRes = new SmsSendDetailPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SmsSendDetailListResponse> list(@RequestBody @Valid SmsSendDetailListRequest smsSendDetailListReq) {
		SmsSendDetailQueryRequest queryReq = new SmsSendDetailQueryRequest();
		KsBeanUtil.copyPropertiesThird(smsSendDetailListReq, queryReq);
		List<SmsSendDetail> smsSendDetailList = smsSendDetailService.list(queryReq);
		List<SmsSendDetailVO> newList = smsSendDetailList.stream().map(entity -> smsSendDetailService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SmsSendDetailListResponse(newList));
	}

	@Override
	public BaseResponse<SmsSendDetailByIdResponse> getById(@RequestBody @Valid SmsSendDetailByIdRequest smsSendDetailByIdRequest) {
		SmsSendDetail smsSendDetail = smsSendDetailService.getById(smsSendDetailByIdRequest.getId());
		return BaseResponse.success(new SmsSendDetailByIdResponse(smsSendDetailService.wrapperVo(smsSendDetail)));
	}

}

