package com.wanmi.sbc.crm.api.provider.preferencetagdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailPageRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailPageResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailListRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailListResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailByIdRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>偏好标签明细查询服务Provider</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@FeignClient(value = "${application.crm.name}", contextId = "PreferenceTagDetailQueryProvider")
public interface PreferenceTagDetailQueryProvider {

	/**
	 * 分页查询偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailPageReq 分页请求参数和筛选对象 {@link PreferenceTagDetailPageRequest}
	 * @return 偏好标签明细分页列表信息 {@link PreferenceTagDetailPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/page")
	BaseResponse<PreferenceTagDetailPageResponse> page(@RequestBody @Valid PreferenceTagDetailPageRequest
                                                               preferenceTagDetailPageReq);

	/**
	 * 列表查询偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailListReq 列表请求参数和筛选对象 {@link PreferenceTagDetailListRequest}
	 * @return 偏好标签明细的列表信息 {@link PreferenceTagDetailListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/list")
	BaseResponse<PreferenceTagDetailListResponse> list(@RequestBody @Valid PreferenceTagDetailListRequest preferenceTagDetailListReq);

	/**
	 * 单个查询偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailByIdRequest 单个查询偏好标签明细请求参数 {@link PreferenceTagDetailByIdRequest}
	 * @return 偏好标签明细详情 {@link PreferenceTagDetailByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/get-by-id")
	BaseResponse<PreferenceTagDetailByIdResponse> getById(@RequestBody @Valid PreferenceTagDetailByIdRequest preferenceTagDetailByIdRequest);

}

