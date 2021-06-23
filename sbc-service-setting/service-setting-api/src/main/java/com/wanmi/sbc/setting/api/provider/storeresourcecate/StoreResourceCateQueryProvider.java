package com.wanmi.sbc.setting.api.provider.storeresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeresourcecate.*;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCatePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺资源资源分类表查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreResourceCateQueryProvider")
public interface StoreResourceCateQueryProvider {

	/**
	 * 分页查询店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCatePageReq 分页请求参数和筛选对象 {@link StoreResourceCatePageRequest}
	 * @return 店铺资源资源分类表分页列表信息 {@link StoreResourceCatePageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/page")
	BaseResponse<StoreResourceCatePageResponse> page(@RequestBody @Valid StoreResourceCatePageRequest
                                                             storeResourceCatePageReq);

	/**
	 * 列表查询店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCateListReq 列表请求参数和筛选对象 {@link StoreResourceCateListRequest}
	 * @return 店铺资源资源分类表的列表信息 {@link StoreResourceCateListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/list")
	BaseResponse<StoreResourceCateListResponse> list(@RequestBody @Valid StoreResourceCateListRequest storeResourceCateListReq);

	/**
	 * 单个查询店铺资源资源分类表API
	 *
	 * @author lq
	 * @param storeResourceCateByIdRequest 单个查询店铺资源资源分类表请求参数 {@link StoreResourceCateByIdRequest}
	 * @return 店铺资源资源分类表详情 {@link StoreResourceCateByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/get-by-id")
	BaseResponse<StoreResourceCateByIdResponse> getById(@RequestBody @Valid StoreResourceCateByIdRequest storeResourceCateByIdRequest);

	/**
	 * 验证是否有子类
	 *
	 * @param storeResourceCateCheckChildRequest 验证是否有子类 {@link StoreResourceCateCheckChildRequest}
	 * @return 验证是否有子类 {@link StoreResourceCateListResponse}
	 * @author lq
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/check-child")
	BaseResponse<Integer> checkChild(@RequestBody @Valid StoreResourceCateCheckChildRequest
											 storeResourceCateCheckChildRequest);

	/**
	 * 验证是否有素材
	 *
	 * @param storeResourceCateCheckResourceRequest 验证是否有素材 {@link StoreResourceCateCheckResourceRequest}
	 * @return 验证是否有素材 {@link StoreResourceCateListResponse}
	 * @author lq
	 */
	@PostMapping("/setting/${application.setting.version}/storeresourcecate/check-resource")
	BaseResponse<Integer> checkResource(@RequestBody @Valid StoreResourceCateCheckResourceRequest storeResourceCateCheckResourceRequest);


}

