package com.wanmi.sbc.marketing.api.provider.grouponcate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdsRequest;
import com.wanmi.sbc.marketing.api.response.grouponcate.GrouponCateByIdResponse;
import com.wanmi.sbc.marketing.api.response.grouponcate.GrouponCateListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>拼团分类信息表查询服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponCateQueryProvider")
public interface GrouponCateQueryProvider {

	/**
	 * 列表查询拼团分类信息表API
	 *
	 * @author groupon
	 * @return 拼团分类信息表的列表信息 {@link GrouponCateListResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/list")
	BaseResponse<GrouponCateListResponse> list();

	/**
	 * 单个查询拼团分类信息表API
	 *
	 * @author groupon
	 * @param grouponCateByIdRequest 单个查询拼团分类信息表请求参数 {@link GrouponCateByIdRequest}
	 * @return 拼团分类信息表详情 {@link GrouponCateByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/get-by-id")
	BaseResponse<GrouponCateByIdResponse> getById(@RequestBody @Valid GrouponCateByIdRequest grouponCateByIdRequest);

	/**
	 * 批量查询拼团分类信息表API
	 *
	 * @param request
	 * @return 拼团分类信息表详情 {@link GrouponCateByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponcate/get-by-ids")
	BaseResponse<GrouponCateListResponse> getByIds(@RequestBody @Valid GrouponCateByIdsRequest request);

}

