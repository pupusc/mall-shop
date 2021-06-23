package com.wanmi.sbc.message.provider.impl.umengtoken;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.umengtoken.UmengTokenSaveProvider;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenAddRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenDelByIdListRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenDelByIdRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenModifyRequest;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenAddResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenModifyResponse;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import com.wanmi.sbc.message.umengtoken.service.UmengTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>友盟推送设备与会员关系保存服务接口实现</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@RestController
@Validated
public class UmengTokenSaveController implements UmengTokenSaveProvider {
	@Autowired
	private UmengTokenService umengTokenService;

	@Override
	public BaseResponse<UmengTokenAddResponse> add(@RequestBody @Valid UmengTokenAddRequest umengTokenAddRequest) {
		UmengToken umengToken = new UmengToken();
		KsBeanUtil.copyPropertiesThird(umengTokenAddRequest, umengToken);
		return BaseResponse.success(new UmengTokenAddResponse(
				umengTokenService.wrapperVo(umengTokenService.add(umengToken))));
	}

	@Override
	public BaseResponse<UmengTokenModifyResponse> modify(@RequestBody @Valid UmengTokenModifyRequest umengTokenModifyRequest) {
		UmengToken umengToken = new UmengToken();
		KsBeanUtil.copyPropertiesThird(umengTokenModifyRequest, umengToken);
		return BaseResponse.success(new UmengTokenModifyResponse(
				umengTokenService.wrapperVo(umengTokenService.modify(umengToken))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid UmengTokenDelByIdRequest umengTokenDelByIdRequest) {
		umengTokenService.deleteById(umengTokenDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid UmengTokenDelByIdListRequest umengTokenDelByIdListRequest) {
		umengTokenService.deleteByIdList(umengTokenDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

