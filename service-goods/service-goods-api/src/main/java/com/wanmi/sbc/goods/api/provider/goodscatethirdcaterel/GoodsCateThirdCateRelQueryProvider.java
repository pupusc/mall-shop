package com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelPageRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelPageResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelListRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelListResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelByIdRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>平台类目和第三方平台类目映射查询服务Provider</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCateThirdCateRelQueryProvider")
public interface GoodsCateThirdCateRelQueryProvider {

	/**
	 * 分页查询平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelPageReq 分页请求参数和筛选对象 {@link GoodsCateThirdCateRelPageRequest}
	 * @return 平台类目和第三方平台类目映射分页列表信息 {@link GoodsCateThirdCateRelPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/page")
	BaseResponse<GoodsCateThirdCateRelPageResponse> page(@RequestBody @Valid GoodsCateThirdCateRelPageRequest goodsCateThirdCateRelPageReq);

	/**
	 * 列表查询平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelListReq 列表请求参数和筛选对象 {@link GoodsCateThirdCateRelListRequest}
	 * @return 平台类目和第三方平台类目映射的列表信息 {@link GoodsCateThirdCateRelListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/list")
	BaseResponse<GoodsCateThirdCateRelListResponse> list(@RequestBody @Valid GoodsCateThirdCateRelListRequest goodsCateThirdCateRelListReq);

	/**
	 * 单个查询平台类目和第三方平台类目映射API
	 *
	 * @author 
	 * @param goodsCateThirdCateRelByIdRequest 单个查询平台类目和第三方平台类目映射请求参数 {@link GoodsCateThirdCateRelByIdRequest}
	 * @return 平台类目和第三方平台类目映射详情 {@link GoodsCateThirdCateRelByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodscatethirdcaterel/get-by-id")
	BaseResponse<GoodsCateThirdCateRelByIdResponse> getById(@RequestBody @Valid GoodsCateThirdCateRelByIdRequest goodsCateThirdCateRelByIdRequest);

}

