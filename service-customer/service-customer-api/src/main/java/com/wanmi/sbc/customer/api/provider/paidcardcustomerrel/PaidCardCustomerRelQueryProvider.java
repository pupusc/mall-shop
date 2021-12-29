package com.wanmi.sbc.customer.api.provider.paidcardcustomerrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.*;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.*;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员查询服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardCustomerRelQueryProvider")
public interface PaidCardCustomerRelQueryProvider {

	/**
	 * 分页查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelPageReq 分页请求参数和筛选对象 {@link PaidCardCustomerRelPageRequest}
	 * @return 付费会员分页列表信息 {@link PaidCardCustomerRelPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/page")
	BaseResponse<PaidCardCustomerRelPageResponse> page(@RequestBody @Valid PaidCardCustomerRelPageRequest paidCardCustomerRelPageReq);


	/**
	 * 根据最大id分页获取付费会员API
	 *
	 * @author duanlsh
	 * @return 付费会员分页列表信息 {@link PaidCardCustomerRelVO}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/page-by-max-auto-id")
	BaseResponse<List<PaidCardCustomerRelVO>> pageByMaxAutoId(@RequestBody @Valid PaidCardCustomerRelQueryRequest request);


	/**
	 * 列表查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelListReq 列表请求参数和筛选对象 {@link PaidCardCustomerRelListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardCustomerRelListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/list")
	BaseResponse<PaidCardCustomerRelListResponse> list(@RequestBody @Valid PaidCardCustomerRelListRequest paidCardCustomerRelListReq);

	/**
	 * 列表查询所有该付费会员下面的会员id
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelListReq 列表请求参数和筛选对象 {@link PaidCardCustomerRelListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardCustomerRelListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/list-by-customer")
	BaseResponse<PaidCardCustomerIdListResponse> listByCustomer(@RequestBody @Valid PaidCardCustomerRelListRequest paidCardCustomerRelListReq);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelByIdRequest 单个查询付费会员请求参数 {@link PaidCardCustomerRelByIdRequest}
	 * @return 付费会员详情 {@link PaidCardCustomerRelByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/get-by-id")
	BaseResponse<PaidCardCustomerRelByIdResponse> getById(@RequestBody @Valid PaidCardCustomerRelByIdRequest paidCardCustomerRelByIdRequest);

	/**
	 *
	 * @param
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/getRelInfo")
	BaseResponse<List<PaidCardCustomerRelVO>> getRelInfo(@RequestBody PaidCardCustomerRelQueryRequest request);

	/**
	 * 查询用户所有可用的付费会员详情
	 *
	 * @author xuhai
	 * @param request 列表请求参数和筛选对象 {@link PaidCardCustomerRelListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardCustomerRelListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/list-customer-rel-full-info")
	BaseResponse<List<PaidCardCustomerRelVO>> listCustomerRelFullInfo( @RequestBody PaidCardCustomerRelListRequest request);

	/**
	 * 分页查询付费会员id
	 *
	 * @author xuhai
	 * @param request 列表请求参数和筛选对象 {@link PaidCustomerIdPageRequest}
	 * @return 付费会员的列表信息 {@link PaidCardCustomerIdPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/list-customer-id-by-pageable")
	BaseResponse<PaidCardCustomerIdPageResponse> listCustomerIdByPageable(@RequestBody PaidCustomerIdPageRequest request);

	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/get-max-discount-paid-card")
	BaseResponse<List<PaidCardVO>> getMaxDiscountPaidCard( @RequestBody @Valid MaxDiscountPaidCardRequest request);
}

