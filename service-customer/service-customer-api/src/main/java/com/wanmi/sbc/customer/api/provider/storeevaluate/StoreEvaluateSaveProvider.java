package com.wanmi.sbc.customer.api.provider.storeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeevaluate.*;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateAddResponse;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺评价保存服务Provider</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreEvaluateSaveProvider")
public interface StoreEvaluateSaveProvider {

	/**
	 * 新增店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateAddRequest 店铺评价新增参数结构 {@link StoreEvaluateAddRequest}
	 * @return 新增的店铺评价信息 {@link StoreEvaluateAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/add")
	BaseResponse<StoreEvaluateAddResponse> add(@RequestBody @Valid StoreEvaluateAddRequest storeEvaluateAddRequest);

	/**
	 *  批量增加店铺评价
	 * @param storeEvaluateAddListRequest
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/add-list")
	BaseResponse addList(@RequestBody StoreEvaluateAddListRequest storeEvaluateAddListRequest);

	/**
	 * 修改店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateModifyRequest 店铺评价修改参数结构 {@link StoreEvaluateModifyRequest}
	 * @return 修改的店铺评价信息 {@link StoreEvaluateModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/modify")
	BaseResponse<StoreEvaluateModifyResponse> modify(@RequestBody @Valid StoreEvaluateModifyRequest storeEvaluateModifyRequest);

	/**
	 * 单个删除店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateDelByIdRequest 单个删除参数结构 {@link StoreEvaluateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreEvaluateDelByIdRequest storeEvaluateDelByIdRequest);

	/**
	 * 批量删除店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateDelByIdListRequest 批量删除参数结构 {@link StoreEvaluateDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storeevaluate/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreEvaluateDelByIdListRequest storeEvaluateDelByIdListRequest);

}

