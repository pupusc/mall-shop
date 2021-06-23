package com.wanmi.sbc.goods.api.provider.storetobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateByIdRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluatePageRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluatePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺服务待评价查询服务Provider</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreTobeEvaluateQueryProvider")
public interface StoreTobeEvaluateQueryProvider {

	/**
	 * 分页查询店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluatePageReq 分页请求参数和筛选对象 {@link StoreTobeEvaluatePageRequest}
	 * @return 店铺服务待评价分页列表信息 {@link StoreTobeEvaluatePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/page")
	BaseResponse<StoreTobeEvaluatePageResponse> page(@RequestBody @Valid StoreTobeEvaluatePageRequest storeTobeEvaluatePageReq);

	/**
	 * 列表查询店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateListReq 列表请求参数和筛选对象 {@link StoreTobeEvaluateListRequest}
	 * @return 店铺服务待评价的列表信息 {@link StoreTobeEvaluateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/list")
	BaseResponse<StoreTobeEvaluateListResponse> list(@RequestBody @Valid StoreTobeEvaluateListRequest storeTobeEvaluateListReq);

	/**
	 * 单个查询店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateByIdRequest 单个查询店铺服务待评价请求参数 {@link StoreTobeEvaluateByIdRequest}
	 * @return 店铺服务待评价详情 {@link StoreTobeEvaluateByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/get-by-id")
	BaseResponse<StoreTobeEvaluateByIdResponse> getById(@RequestBody @Valid StoreTobeEvaluateByIdRequest storeTobeEvaluateByIdRequest);

	/**
	 * 查询店铺评价服务数量
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/get-store-tobe-evaluate-num")
	BaseResponse<Long> getStoreTobeEvaluateNum(@RequestBody StoreTobeEvaluateQueryRequest queryReq);

	/**
	 * @Author lvzhenwei
	 * @Description 待评价店铺服务自动评价
	 * @Date 10:36 2019/4/25
	 * @Param []
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/auto-store-evaluate")
	BaseResponse autoStoreEvaluate();

}

