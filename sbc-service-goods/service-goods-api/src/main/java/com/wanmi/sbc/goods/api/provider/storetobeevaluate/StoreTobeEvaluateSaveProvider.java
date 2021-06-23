package com.wanmi.sbc.goods.api.provider.storetobeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.*;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺服务待评价保存服务Provider</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreTobeEvaluateSaveProvider")
public interface StoreTobeEvaluateSaveProvider {

	/**
	 * 新增店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateAddRequest 店铺服务待评价新增参数结构 {@link StoreTobeEvaluateAddRequest}
	 * @return 新增的店铺服务待评价信息 {@link StoreTobeEvaluateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/add")
	BaseResponse<StoreTobeEvaluateAddResponse> add(@RequestBody @Valid StoreTobeEvaluateAddRequest storeTobeEvaluateAddRequest);

	/**
	 * 修改店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateModifyRequest 店铺服务待评价修改参数结构 {@link StoreTobeEvaluateModifyRequest}
	 * @return 修改的店铺服务待评价信息 {@link StoreTobeEvaluateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/modify")
	BaseResponse<StoreTobeEvaluateModifyResponse> modify(@RequestBody @Valid StoreTobeEvaluateModifyRequest storeTobeEvaluateModifyRequest);

	/**
	 * 单个删除店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateDelByIdRequest 单个删除参数结构 {@link StoreTobeEvaluateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreTobeEvaluateDelByIdRequest storeTobeEvaluateDelByIdRequest);

	/**
	 * 批量删除店铺服务待评价API
	 *
	 * @author lzw
	 * @param storeTobeEvaluateDelByIdListRequest 批量删除参数结构 {@link StoreTobeEvaluateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreTobeEvaluateDelByIdListRequest storeTobeEvaluateDelByIdListRequest);

	/**
	 * @Description: 订单ID和店铺ID删除
	 * @Author: Bob
	 * @Date: 2019-04-12 16:29
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/delete-by-order-sku")
	BaseResponse<Integer> deleteByOrderAndStoreId(@RequestBody @Valid StoreTobeEvaluateQueryRequest storeTobeEvaluateQueryRequest);

	/**
	 * @Description: 动态条件查询
	 * @Author: Bob
	 * @Date: 2019-04-12 17:19
	 */
	@PostMapping("/goods/${application.goods.version}/storetobeevaluate/query")
	BaseResponse<StoreTobeEvaluateAddResponse> query(@RequestBody @Valid StoreTobeEvaluateQueryRequest request);
}

