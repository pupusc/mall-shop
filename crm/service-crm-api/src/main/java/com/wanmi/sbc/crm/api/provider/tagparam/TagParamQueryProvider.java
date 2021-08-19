package com.wanmi.sbc.crm.api.provider.tagparam;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamPageRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamPageResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamListRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamListResponse;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamByIdRequest;
import com.wanmi.sbc.crm.api.response.tagparam.TagParamByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>标签参数查询服务Provider</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@FeignClient(value = "${application.crm.name}", contextId = "TagParamQueryProvider")
public interface TagParamQueryProvider {

	/**
	 * 分页查询标签参数API
	 *
	 * @author dyt
	 * @param tagParamPageReq 分页请求参数和筛选对象 {@link TagParamPageRequest}
	 * @return 标签参数分页列表信息 {@link TagParamPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/page")
	BaseResponse<TagParamPageResponse> page(@RequestBody @Valid TagParamPageRequest tagParamPageReq);

	/**
	 * 列表查询标签参数API
	 *
	 * @author dyt
	 * @param tagParamListReq 列表请求参数和筛选对象 {@link TagParamListRequest}
	 * @return 标签参数的列表信息 {@link TagParamListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/list")
	BaseResponse<TagParamListResponse> list(@RequestBody @Valid TagParamListRequest tagParamListReq);

	/**
	 * 单个查询标签参数API
	 *
	 * @author dyt
	 * @param tagParamByIdRequest 单个查询标签参数请求参数 {@link TagParamByIdRequest}
	 * @return 标签参数详情 {@link TagParamByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagparam/get-by-id")
	BaseResponse<TagParamByIdResponse> getById(@RequestBody @Valid TagParamByIdRequest tagParamByIdRequest);

}

