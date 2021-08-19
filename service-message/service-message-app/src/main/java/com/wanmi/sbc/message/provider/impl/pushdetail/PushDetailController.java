package com.wanmi.sbc.message.provider.impl.pushdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.pushdetail.PushDetailProvider;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailAddRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailModifyRequest;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailAddResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailModifyResponse;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import com.wanmi.sbc.message.pushdetail.service.PushDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>推送详情保存服务接口实现</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@RestController
@Validated
public class PushDetailController implements PushDetailProvider {
	@Autowired
	private PushDetailService pushDetailService;

	@Override
	public BaseResponse<PushDetailAddResponse> add(@RequestBody @Valid PushDetailAddRequest pushDetailAddRequest) {
		PushDetail pushDetail = KsBeanUtil.convert(pushDetailAddRequest, PushDetail.class);
		return BaseResponse.success(new PushDetailAddResponse(
				pushDetailService.wrapperVo(pushDetailService.add(pushDetail))));
	}

	@Override
	public BaseResponse<PushDetailModifyResponse> modify(@RequestBody @Valid PushDetailModifyRequest pushDetailModifyRequest) {
		PushDetail pushDetail = KsBeanUtil.convert(pushDetailModifyRequest, PushDetail.class);
		return BaseResponse.success(new PushDetailModifyResponse(
				pushDetailService.wrapperVo(pushDetailService.modify(pushDetail))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PushDetailDelByIdRequest pushDetailDelByIdRequest) {
		pushDetailService.deleteById(pushDetailDelByIdRequest.getTaskId());
		return BaseResponse.SUCCESSFUL();
	}

}

