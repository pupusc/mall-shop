package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SensitiveWordsBadWordRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsByIdRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsListRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsQueryRequest;
import com.wanmi.sbc.setting.api.response.SensitiveWordsByIdResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsListResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Set;

/**
 * <p>查询服务Provider</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@FeignClient(value = "${application.setting.name}", contextId = "SensitiveWordsQueryProvider")
public interface SensitiveWordsQueryProvider {

	/**
	 * 分页查询API
	 *
	 * @author wjj
	 * @param sensitiveWordsPageReq 分页请求参数和筛选对象 {@link SensitiveWordsQueryRequest}
	 * @return 分页列表信息 {@link SensitiveWordsPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/page")
	BaseResponse<SensitiveWordsPageResponse> page(@RequestBody @Valid SensitiveWordsQueryRequest sensitiveWordsPageReq);

	/**
	 * 列表查询API
	 *
	 * @author wjj
	 * @param sensitiveWordsListReq 列表请求参数和筛选对象 {@link SensitiveWordsListRequest}
	 * @return 的列表信息 {@link SensitiveWordsListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/list")
	BaseResponse<SensitiveWordsListResponse> list(@RequestBody @Valid SensitiveWordsListRequest sensitiveWordsListReq);

	/**
	 * 单个查询API
	 *
	 * @author wjj
	 * @param sensitiveWordsByIdRequest 单个查询请求参数 {@link SensitiveWordsByIdRequest}
	 * @return 详情 {@link SensitiveWordsByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/get-by-id")
	BaseResponse<SensitiveWordsByIdResponse> getById(@RequestBody @Valid SensitiveWordsByIdRequest
															 sensitiveWordsByIdRequest);

 	@PostMapping("/setting/${application.setting.version}/sensitivewords/get-bad-word")
	BaseResponse<Set<String>> getBadWord(@RequestBody SensitiveWordsBadWordRequest request);

	@PostMapping("/setting/${application.setting.version}/sensitivewords/add-bad-word")
	BaseResponse addBadWordToHashMap(@RequestBody SensitiveWordsBadWordRequest request);
}

