package com.wanmi.sbc.crm.api.provider.tagdimension;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionBigJsonRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionByIdRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionListRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionPageRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionBigJsonResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionByIdResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionListResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>标签维度查询服务Provider</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@FeignClient(value = "${application.crm.name}", contextId = "TagDimensionQueryProvider")
public interface TagDimensionQueryProvider {

	/**
	 * 分页查询标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionPageReq 分页请求参数和筛选对象 {@link TagDimensionPageRequest}
	 * @return 标签维度分页列表信息 {@link TagDimensionPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/page")
	BaseResponse<TagDimensionPageResponse> page(@RequestBody @Valid TagDimensionPageRequest tagDimensionPageReq);

	/**
	 * 列表查询标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionListReq 列表请求参数和筛选对象 {@link TagDimensionListRequest}
	 * @return 标签维度的列表信息 {@link TagDimensionListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/list")
	BaseResponse<TagDimensionListResponse> list(@RequestBody @Valid TagDimensionListRequest tagDimensionListReq);

	/**
	 * 单个查询标签维度API
	 *
	 * @author dyt
	 * @param tagDimensionByIdRequest 单个查询标签维度请求参数 {@link TagDimensionByIdRequest}
	 * @return 标签维度详情 {@link TagDimensionByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/get-by-id")
	BaseResponse<TagDimensionByIdResponse> getById(@RequestBody @Valid TagDimensionByIdRequest tagDimensionByIdRequest);

	/**
	 *
	 * 获取指行和行为初始化数据
	 * @author
	 * @return 标签维度详情 {@link TagDimensionBigJsonResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/get-big-json")
	BaseResponse<TagDimensionBigJsonResponse> getBigJson(@RequestBody @Valid TagDimensionBigJsonRequest request);

	/**
	 *
	 * 获取指行和行为初始化数据
	 * @author
	 * @return 标签维度详情 {@link TagDimensionBigJsonResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/tagdimension/get-preference-big-json")
	BaseResponse<TagDimensionBigJsonResponse> getPreferenceBigJson();




}

