package com.wanmi.sbc.customer.api.provider.storeevaluatenum;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumAddRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumDelByIdListRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumModifyRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumAddRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumAddResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumModifyResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺统计评分等级人数统计保存服务Provider</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateNumSaveProvider")
public interface StoreEvaluateNumSaveProvider {

	/**
	 * 新增店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumAddRequest 店铺统计评分等级人数统计新增参数结构 {@link StoreEvaluateNumAddRequest}
	 * @return 新增的店铺统计评分等级人数统计信息 {@link StoreEvaluateNumAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/add")
	BaseResponse<StoreEvaluateNumAddResponse> add(@RequestBody @Valid StoreEvaluateNumAddRequest storeEvaluateNumAddRequest);

	/**
	 * 修改店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumModifyRequest 店铺统计评分等级人数统计修改参数结构 {@link StoreEvaluateNumModifyRequest}
	 * @return 修改的店铺统计评分等级人数统计信息 {@link StoreEvaluateNumModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/modify")
	BaseResponse<StoreEvaluateNumModifyResponse> modify(@RequestBody @Valid StoreEvaluateNumModifyRequest storeEvaluateNumModifyRequest);

	/**
	 * 单个删除店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumDelByIdRequest 单个删除参数结构 {@link StoreEvaluateNumDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreEvaluateNumDelByIdRequest storeEvaluateNumDelByIdRequest);

	/**
	 * 批量删除店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumDelByIdListRequest 批量删除参数结构 {@link StoreEvaluateNumDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateNumDelByIdListRequest storeEvaluateNumDelByIdListRequest);


	/**
	 * 批量新增店铺统计评分等级人数统计API
	 *
	 * @author liutao
	 * @param storeEvaluateNumAddRequests 店铺统计评分等级人数统计新增参数结构 {@link StoreEvaluateSumAddRequest}
	 * @return 新增的店铺评价信息 {@link StoreEvaluateSumAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/add-list")
	BaseResponse<StoreEvaluateNumAddResponse> addList(@RequestBody @Valid List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests);


	/**
	 * 删除所有数据
	 *
	 * @author liutao
	 * @return 删除所有 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatenum/delete-all")
	BaseResponse deleteAll();

}

