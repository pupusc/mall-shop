package com.wanmi.sbc.setting.api.provider.storeresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourcePageRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourcePageResponse;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceListRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceListResponse;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceByIdRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺资源库查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreResourceQueryProvider")
public interface StoreResourceQueryProvider {

	/**
	 * 分页查询店铺资源库API
	 *
	 * @author lq
	 * @param storeResourcePageReq 分页请求参数和筛选对象 {@link StoreResourcePageRequest}
	 * @return 店铺资源库分页列表信息 {@link StoreResourcePageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/page")
	BaseResponse<StoreResourcePageResponse> page(@RequestBody @Valid StoreResourcePageRequest storeResourcePageReq);

	/**
	 * 列表查询店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceListReq 列表请求参数和筛选对象 {@link StoreResourceListRequest}
	 * @return 店铺资源库的列表信息 {@link StoreResourceListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/list")
	BaseResponse<StoreResourceListResponse> list(@RequestBody @Valid StoreResourceListRequest storeResourceListReq);

	/**
	 * 单个查询店铺资源库API
	 *
	 * @author lq
	 * @param storeResourceByIdRequest 单个查询店铺资源库请求参数 {@link StoreResourceByIdRequest}
	 * @return 店铺资源库详情 {@link StoreResourceByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresource/get-by-id")
	BaseResponse<StoreResourceByIdResponse> getById(@RequestBody @Valid StoreResourceByIdRequest
                                                            storeResourceByIdRequest);

}

