package com.wanmi.sbc.message.provider.impl.umengtoken;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.provider.umengtoken.UmengTokenQueryProvider;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenByIdRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenListRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenPageRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenQueryRequest;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenByIdResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenListResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenPageResponse;
import com.wanmi.sbc.message.bean.vo.UmengTokenVO;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import com.wanmi.sbc.message.umengtoken.service.UmengTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>友盟推送设备与会员关系查询服务接口实现</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@RestController
@Validated
public class UmengTokenQueryController implements UmengTokenQueryProvider {
	@Autowired
	private UmengTokenService umengTokenService;

	@Override
	public BaseResponse<UmengTokenPageResponse> page(@RequestBody @Valid UmengTokenPageRequest umengTokenPageReq) {
		UmengTokenQueryRequest queryReq = new UmengTokenQueryRequest();
		KsBeanUtil.copyPropertiesThird(umengTokenPageReq, queryReq);
		Page<UmengToken> umengTokenPage = umengTokenService.page(queryReq);
		Page<UmengTokenVO> newPage = umengTokenPage.map(entity -> umengTokenService.wrapperVo(entity));
		MicroServicePage<UmengTokenVO> microPage = new MicroServicePage<>(newPage, umengTokenPageReq.getPageable());
		UmengTokenPageResponse finalRes = new UmengTokenPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<UmengTokenListResponse> list(@RequestBody @Valid UmengTokenListRequest umengTokenListReq) {
		UmengTokenQueryRequest queryReq = new UmengTokenQueryRequest();
		KsBeanUtil.copyPropertiesThird(umengTokenListReq, queryReq);
		List<UmengToken> umengTokenList = umengTokenService.list(queryReq);
		List<UmengTokenVO> newList = umengTokenList.stream().map(entity -> umengTokenService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new UmengTokenListResponse(newList));
	}

	@Override
	public BaseResponse<UmengTokenByIdResponse> getById(@RequestBody @Valid UmengTokenByIdRequest umengTokenByIdRequest) {
		Optional<UmengToken> umengToken = umengTokenService.getById(umengTokenByIdRequest.getId());
		return BaseResponse.success(new UmengTokenByIdResponse(umengTokenService.wrapperVo(umengToken.orElse(new UmengToken()))));
	}

}

