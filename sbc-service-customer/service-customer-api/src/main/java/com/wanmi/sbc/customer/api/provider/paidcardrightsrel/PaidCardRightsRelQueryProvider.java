package com.wanmi.sbc.customer.api.provider.paidcardrightsrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelPageRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelPageResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelListRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelListResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员查询服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardRightsRelQueryProvider")
public interface PaidCardRightsRelQueryProvider {

	/**
	 * 分页查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelPageReq 分页请求参数和筛选对象 {@link PaidCardRightsRelPageRequest}
	 * @return 付费会员分页列表信息 {@link PaidCardRightsRelPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/page")
	BaseResponse<PaidCardRightsRelPageResponse> page(@RequestBody @Valid PaidCardRightsRelPageRequest paidCardRightsRelPageReq);

	/**
	 * 列表查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelListReq 列表请求参数和筛选对象 {@link PaidCardRightsRelListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardRightsRelListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/list")
	BaseResponse<PaidCardRightsRelListResponse> list(@RequestBody @Valid PaidCardRightsRelListRequest paidCardRightsRelListReq);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelByIdRequest 单个查询付费会员请求参数 {@link PaidCardRightsRelByIdRequest}
	 * @return 付费会员详情 {@link PaidCardRightsRelByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/get-by-id")
	BaseResponse<PaidCardRightsRelByIdResponse> getById(@RequestBody @Valid PaidCardRightsRelByIdRequest paidCardRightsRelByIdRequest);

}

