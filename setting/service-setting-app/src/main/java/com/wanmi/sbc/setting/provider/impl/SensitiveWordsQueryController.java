package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.setting.api.request.SensitiveWordsBadWordRequest;
import com.wanmi.sbc.setting.sensitivewords.service.BadWordService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsQueryProvider;
import com.wanmi.sbc.setting.api.request.SensitiveWordsQueryRequest;
import com.wanmi.sbc.setting.api.response.SensitiveWordsPageResponse;
import com.wanmi.sbc.setting.api.request.SensitiveWordsListRequest;
import com.wanmi.sbc.setting.api.response.SensitiveWordsListResponse;
import com.wanmi.sbc.setting.api.request.SensitiveWordsByIdRequest;
import com.wanmi.sbc.setting.api.response.SensitiveWordsByIdResponse;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import com.wanmi.sbc.setting.sensitivewords.service.SensitiveWordsService;
import com.wanmi.sbc.setting.sensitivewords.model.root.SensitiveWords;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>查询服务接口实现</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@RestController
@Validated
public class SensitiveWordsQueryController implements SensitiveWordsQueryProvider {
	@Autowired
	private SensitiveWordsService sensitiveWordsService;

	@Autowired
	private BadWordService badWordService;
	@Override
	public BaseResponse<SensitiveWordsPageResponse> page(@RequestBody @Valid SensitiveWordsQueryRequest sensitiveWordsPageReq) {
		SensitiveWordsQueryRequest queryReq = new SensitiveWordsQueryRequest();
		KsBeanUtil.copyPropertiesThird(sensitiveWordsPageReq, queryReq);
		Page<SensitiveWords> sensitiveWordsPage = sensitiveWordsService.page(queryReq);
		Page<SensitiveWordsVO> newPage = sensitiveWordsPage.map(entity -> sensitiveWordsService.wrapperVo(entity));
		MicroServicePage<SensitiveWordsVO> microPage = new MicroServicePage<>(newPage, sensitiveWordsPageReq.getPageable());
		SensitiveWordsPageResponse finalRes = new SensitiveWordsPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SensitiveWordsListResponse> list(@RequestBody @Valid SensitiveWordsListRequest sensitiveWordsListReq) {
		SensitiveWordsQueryRequest queryReq = new SensitiveWordsQueryRequest();
		KsBeanUtil.copyPropertiesThird(sensitiveWordsListReq, queryReq);
		List<SensitiveWords> sensitiveWordsList = sensitiveWordsService.list(queryReq);
		List<SensitiveWordsVO> newList = sensitiveWordsList.stream().map(entity -> sensitiveWordsService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new SensitiveWordsListResponse(newList));
	}

	@Override
	public BaseResponse<SensitiveWordsByIdResponse> getById(@RequestBody @Valid SensitiveWordsByIdRequest sensitiveWordsByIdRequest) {
		SensitiveWords sensitiveWords = sensitiveWordsService.getById(sensitiveWordsByIdRequest.getSensitiveId());
		return BaseResponse.success(new SensitiveWordsByIdResponse(sensitiveWordsService.wrapperVo(sensitiveWords)));
	}

	@Override
	public BaseResponse<Set<String>> getBadWord(@RequestBody SensitiveWordsBadWordRequest
															sensitiveWordsBadwordrequest) {
		badWordService.addBadWordToHashMap(false);
		return  BaseResponse.success(badWordService.getBadWord(sensitiveWordsBadwordrequest.getTxt(),1));
	}

	@Override
	public BaseResponse addBadWordToHashMap(@RequestBody SensitiveWordsBadWordRequest request) {
		badWordService.addBadWordToHashMap(false);
		return  BaseResponse.SUCCESSFUL();
	}

}

