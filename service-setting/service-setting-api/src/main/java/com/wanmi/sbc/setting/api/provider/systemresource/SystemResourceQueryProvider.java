package com.wanmi.sbc.setting.api.provider.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourcePageRequest;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourcePageResponse;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceListRequest;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceListResponse;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceByIdRequest;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>平台素材资源查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemResourceQueryProvider")
public interface SystemResourceQueryProvider {

	/**
	 * 分页查询平台素材资源API
	 *
	 * @author lq
	 * @param systemResourcePageReq 分页请求参数和筛选对象 {@link SystemResourcePageRequest}
	 * @return 平台素材资源分页列表信息 {@link SystemResourcePageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresource/page")
	BaseResponse<SystemResourcePageResponse> page(@RequestBody @Valid SystemResourcePageRequest systemResourcePageReq);

	/**
	 * 列表查询平台素材资源API
	 *
	 * @author lq
	 * @param systemResourceListReq 列表请求参数和筛选对象 {@link SystemResourceListRequest}
	 * @return 平台素材资源的列表信息 {@link SystemResourceListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresource/list")
	BaseResponse<SystemResourceListResponse> list(@RequestBody @Valid SystemResourceListRequest systemResourceListReq);

	/**
	 * 单个查询平台素材资源API
	 *
	 * @author lq
	 * @param systemResourceByIdRequest 单个查询平台素材资源请求参数 {@link SystemResourceByIdRequest}
	 * @return 平台素材资源详情 {@link SystemResourceByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemresource/get-by-id")
	BaseResponse<SystemResourceByIdResponse> getById(@RequestBody @Valid SystemResourceByIdRequest
                                                             systemResourceByIdRequest);

}

