package com.wanmi.sbc.customer.api.provider.storereturnaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺退货地址表保存服务Provider</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreReturnAddressProvider")
public interface StoreReturnAddressProvider {

	/**
	 * 新增店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressAddRequest 店铺退货地址表新增参数结构 {@link StoreReturnAddressAddRequest}
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/add")
	BaseResponse add(@RequestBody @Valid StoreReturnAddressAddRequest storeReturnAddressAddRequest);

	/**
	 * 修改店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressModifyRequest 店铺退货地址表修改参数结构 {@link StoreReturnAddressModifyRequest}
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/modify")
	BaseResponse modify(@RequestBody @Valid StoreReturnAddressModifyRequest storeReturnAddressModifyRequest);

	/**
	 * 默认店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressDefaultRequest 店铺退货地址表修改参数结构 {@link StoreReturnAddressDefaultRequest}
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/modify-default")
	BaseResponse modifyDefault(@RequestBody @Valid StoreReturnAddressDefaultRequest storeReturnAddressDefaultRequest);

	/**
	 * 单个删除店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressDelByIdRequest 单个删除参数结构 {@link StoreReturnAddressDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid StoreReturnAddressDelByIdRequest storeReturnAddressDelByIdRequest);

	/**
	 * 批量删除店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressDelByIdListRequest 批量删除参数结构 {@link StoreReturnAddressDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid StoreReturnAddressDelByIdListRequest storeReturnAddressDelByIdListRequest);

	/**
	 * 批量新增店铺
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/batch-add")
	BaseResponse batchAdd();

}

