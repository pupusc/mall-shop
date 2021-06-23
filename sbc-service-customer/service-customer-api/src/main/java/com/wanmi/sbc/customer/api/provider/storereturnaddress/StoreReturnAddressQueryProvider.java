package com.wanmi.sbc.customer.api.provider.storereturnaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressPageRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressPageResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressListRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressListResponse;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressByIdRequest;
import com.wanmi.sbc.customer.api.response.storereturnaddress.StoreReturnAddressByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>店铺退货地址表查询服务Provider</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreReturnAddressQueryProvider")
public interface StoreReturnAddressQueryProvider {

	/**
	 * 分页查询店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressPageReq 分页请求参数和筛选对象 {@link StoreReturnAddressPageRequest}
	 * @return 店铺退货地址表分页列表信息 {@link StoreReturnAddressPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/page")
	BaseResponse<StoreReturnAddressPageResponse> page(@RequestBody @Valid StoreReturnAddressPageRequest storeReturnAddressPageReq);

	/**
	 * 列表查询店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressListReq 列表请求参数和筛选对象 {@link StoreReturnAddressListRequest}
	 * @return 店铺退货地址表的列表信息 {@link StoreReturnAddressListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/list")
	BaseResponse<StoreReturnAddressListResponse> list(@RequestBody @Valid StoreReturnAddressListRequest storeReturnAddressListReq);

	/**
	 * 单个查询店铺退货地址表API
	 *
	 * @author dyt
	 * @param storeReturnAddressByIdRequest 单个查询店铺退货地址表请求参数 {@link StoreReturnAddressByIdRequest}
	 * @return 店铺退货地址表详情 {@link StoreReturnAddressByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/storereturnaddress/get-by-id")
	BaseResponse<StoreReturnAddressByIdResponse> getById(@RequestBody @Valid StoreReturnAddressByIdRequest storeReturnAddressByIdRequest);

}

