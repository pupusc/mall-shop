package com.wanmi.sbc.crm.api.provider.tagparam;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamAddRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamAddResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamModifyRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamModifyResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamDelByIdRequest;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>标签参数保存服务Provider</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@FeignClient(value = "${application.crm.name}", contextId = "TagParamProvider")
public interface TagParamProvider {

	/**
	 * 新增标签参数API
	 *
	 * @author dyt
	 * @param tagParamAddRequest 标签参数新增参数结构 {@link TagParamAddRequest}
	 * @return 新增的标签参数信息 {@link TagParamAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/add")
	BaseResponse<TagParamAddResponse> add(@RequestBody @Valid TagParamAddRequest tagParamAddRequest);

	/**
	 * 修改标签参数API
	 *
	 * @author dyt
	 * @param tagParamModifyRequest 标签参数修改参数结构 {@link TagParamModifyRequest}
	 * @return 修改的标签参数信息 {@link TagParamModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/modify")
	BaseResponse<TagParamModifyResponse> modify(@RequestBody @Valid TagParamModifyRequest tagParamModifyRequest);

	/**
	 * 单个删除标签参数API
	 *
	 * @author dyt
	 * @param tagParamDelByIdRequest 单个删除参数结构 {@link TagParamDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid TagParamDelByIdRequest tagParamDelByIdRequest);

	/**
	 * 批量删除标签参数API
	 *
	 * @author dyt
	 * @param tagParamDelByIdListRequest 批量删除参数结构 {@link TagParamDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid TagParamDelByIdListRequest tagParamDelByIdListRequest);

}

