package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.SensitiveWordsAddResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>保存服务Provider</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@FeignClient(value = "${application.setting.name}", contextId = "SensitiveWordsSaveProvider")
public interface SensitiveWordsSaveProvider {

	/**
	 * 新增API
	 *
	 * @author wjj
	 * @param sensitiveWordsAddRequest 新增参数结构 {@link SensitiveWordsAddRequest}
	 * @return 新增的信息 {@link SensitiveWordsAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/add")
	BaseResponse<SensitiveWordsSaveResponse> add(@RequestBody @Valid SensitiveWordsAddRequest sensitiveWordsAddRequest);

	/**
	 * 修改API
	 *
	 * @author wjj
	 * @param sensitiveWordsModifyRequest 修改参数结构 {@link SensitiveWordsModifyRequest}
	 * @return 修改的信息 {@link SensitiveWordsModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/modify")
	BaseResponse<SensitiveWordsModifyResponse> modify(@RequestBody @Valid SensitiveWordsModifyRequest
                                                              sensitiveWordsModifyRequest);

	/**
	 * 单个删除API
	 *
	 * @author wjj
	 * @param sensitiveWordsDelByIdRequest 单个删除参数结构 {@link SensitiveWordsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SensitiveWordsDelByIdRequest sensitiveWordsDelByIdRequest);

	/**
	 * 批量删除API
	 *
	 * @author wjj
	 * @param sensitiveWordsDelByIdListRequest 批量删除参数结构 {@link SensitiveWordsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/sensitivewords/delete-by-id-list")
	BaseResponse<SensitiveWordsDeleteResponse> deleteByIdList(@RequestBody @Valid SensitiveWordsDelByIdListRequest sensitiveWordsDelByIdListRequest);

}

