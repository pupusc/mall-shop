package com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaPageRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaPageResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaByIdRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺快递公司关联表查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@FeignClient(value = "${application.setting.name}", contextId = "StoreExpressCompanyRelaQueryProvider")
public interface StoreExpressCompanyRelaQueryProvider {

	/**
	 * 分页查询店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaPageReq 分页请求参数和筛选对象 {@link StoreExpressCompanyRelaPageRequest}
	 * @return 店铺快递公司关联表分页列表信息 {@link StoreExpressCompanyRelaPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/page")
	BaseResponse<StoreExpressCompanyRelaPageResponse> page(@RequestBody @Valid StoreExpressCompanyRelaPageRequest
                                                                   storeExpressCompanyRelaPageReq);

	/**
	 * 查询店铺正在使用的物流公司列表
	 * @author lq
	 * @param storeExpressCompanyRelaListReq 列表请求参数和筛选对象 {@link StoreExpressCompanyRelaListRequest}
	 * @return 店铺快递公司关联表的列表信息 {@link StoreExpressCompanyRelaListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/list")
	BaseResponse<StoreExpressCompanyRelaListResponse> list(@RequestBody @Valid StoreExpressCompanyRelaListRequest storeExpressCompanyRelaListReq);

	/**
	 * 单个查询店铺快递公司关联表API
	 *
	 * @author lq
	 * @param storeExpressCompanyRelaByIdRequest 单个查询店铺快递公司关联表请求参数 {@link StoreExpressCompanyRelaByIdRequest}
	 * @return 店铺快递公司关联表详情 {@link StoreExpressCompanyRelaByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storeexpresscompanyrela/get-by-id")
	BaseResponse<StoreExpressCompanyRelaByIdResponse> getById(@RequestBody @Valid StoreExpressCompanyRelaByIdRequest storeExpressCompanyRelaByIdRequest);
}

