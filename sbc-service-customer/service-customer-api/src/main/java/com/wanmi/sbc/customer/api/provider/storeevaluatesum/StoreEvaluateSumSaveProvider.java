package com.wanmi.sbc.customer.api.provider.storeevaluatesum;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumAddRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumAddResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumModifyRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumModifyResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺评价保存服务Provider</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateSumSaveProvider")
public interface StoreEvaluateSumSaveProvider {

	/**
	 * 新增店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumAddRequest 店铺评价新增参数结构 {@link StoreEvaluateSumAddRequest}
	 * @return 新增的店铺评价信息 {@link StoreEvaluateSumAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/add")
	BaseResponse<StoreEvaluateSumAddResponse> add(@RequestBody @Valid StoreEvaluateSumAddRequest storeEvaluateSumAddRequest);

	/**
	 * 修改店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumModifyRequest 店铺评价修改参数结构 {@link StoreEvaluateSumModifyRequest}
	 * @return 修改的店铺评价信息 {@link StoreEvaluateSumModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/modify")
	BaseResponse<StoreEvaluateSumModifyResponse> modify(@RequestBody @Valid StoreEvaluateSumModifyRequest storeEvaluateSumModifyRequest);

	/**
	 * 单个删除店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumDelByIdRequest 单个删除参数结构 {@link StoreEvaluateSumDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreEvaluateSumDelByIdRequest storeEvaluateSumDelByIdRequest);

	/**
	 * 批量删除店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumDelByIdListRequest 批量删除参数结构 {@link StoreEvaluateSumDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateSumDelByIdListRequest storeEvaluateSumDelByIdListRequest);


	/**
	 * 批量新增店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumAddRequestList 店铺评价新增参数结构 {@link StoreEvaluateSumAddRequest}
	 * @return 新增的店铺评价信息 {@link StoreEvaluateSumAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/add-list")
	BaseResponse<StoreEvaluateSumAddResponse> addList(@RequestBody @Valid List<StoreEvaluateSumAddRequest> storeEvaluateSumAddRequestList);


	/**
	 * 删除所有数据
	 *
	 * @author liutao
	 * @return 删除所有 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluatesum/delete-all")
	BaseResponse deleteAll();

}

