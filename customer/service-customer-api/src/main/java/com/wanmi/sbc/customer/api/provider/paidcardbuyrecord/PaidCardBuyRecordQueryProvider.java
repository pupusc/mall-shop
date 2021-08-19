package com.wanmi.sbc.customer.api.provider.paidcardbuyrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordPageRequest;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordQueryRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordPageResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordListRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordListResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员查询服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardBuyRecordQueryProvider")
public interface PaidCardBuyRecordQueryProvider {

	/**
	 * 分页查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordPageReq 分页请求参数和筛选对象 {@link PaidCardBuyRecordPageRequest}
	 * @return 付费会员分页列表信息 {@link PaidCardBuyRecordPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/page")
	BaseResponse<PaidCardBuyRecordPageResponse> page(@RequestBody @Valid PaidCardBuyRecordPageRequest paidCardBuyRecordPageReq);

	/**
	 * 列表查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordListReq 列表请求参数和筛选对象 {@link PaidCardBuyRecordListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardBuyRecordListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/list")
	BaseResponse<PaidCardBuyRecordListResponse> list(@RequestBody @Valid PaidCardBuyRecordListRequest paidCardBuyRecordListReq);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordByIdRequest 单个查询付费会员请求参数 {@link PaidCardBuyRecordByIdRequest}
	 * @return 付费会员详情 {@link PaidCardBuyRecordByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/get-by-id")
	BaseResponse<PaidCardBuyRecordByIdResponse> getById(@RequestBody @Valid PaidCardBuyRecordByIdRequest paidCardBuyRecordByIdRequest);

}

