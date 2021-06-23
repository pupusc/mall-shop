package com.wanmi.sbc.customer.api.provider.paidcardrule;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRulePageRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRulePageResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleListRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleListResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员查询服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardRuleQueryProvider")
public interface PaidCardRuleQueryProvider {

	/**
	 * 分页查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRulePageReq 分页请求参数和筛选对象 {@link PaidCardRulePageRequest}
	 * @return 付费会员分页列表信息 {@link PaidCardRulePageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/page")
	BaseResponse<PaidCardRulePageResponse> page(@RequestBody @Valid PaidCardRulePageRequest paidCardRulePageReq);

	/**
	 * 列表查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleListReq 列表请求参数和筛选对象 {@link PaidCardRuleListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardRuleListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/list")
	BaseResponse<PaidCardRuleListResponse> list(@RequestBody @Valid PaidCardRuleListRequest paidCardRuleListReq);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleByIdRequest 单个查询付费会员请求参数 {@link PaidCardRuleByIdRequest}
	 * @return 付费会员详情 {@link PaidCardRuleByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/get-by-id")
	BaseResponse<PaidCardRuleByIdResponse> getById(@RequestBody @Valid PaidCardRuleByIdRequest paidCardRuleByIdRequest);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleByIdRequest 单个查询付费会员请求参数 {@link PaidCardRuleByIdRequest}
	 * @return 付费会员详情 {@link PaidCardRuleByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/get-by-id-with-paid-card-info")
	BaseResponse<PaidCardRuleByIdResponse> getByIdWithPaidCardInfo(@RequestBody @Valid PaidCardRuleByIdRequest paidCardRuleByIdRequest);

}

