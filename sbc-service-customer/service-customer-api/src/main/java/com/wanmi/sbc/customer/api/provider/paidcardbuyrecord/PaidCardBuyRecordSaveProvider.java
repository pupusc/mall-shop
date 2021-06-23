package com.wanmi.sbc.customer.api.provider.paidcardbuyrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardBuyRecordSaveProvider")
public interface PaidCardBuyRecordSaveProvider {

	/**
	 * 新增付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordAddRequest 付费会员新增参数结构 {@link PaidCardBuyRecordAddRequest}
	 * @return 新增的付费会员信息 {@link PaidCardBuyRecordAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/add")
	BaseResponse<PaidCardBuyRecordAddResponse> add(@RequestBody @Valid PaidCardBuyRecordAddRequest paidCardBuyRecordAddRequest);

	/**
	 * 修改付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordModifyRequest 付费会员修改参数结构 {@link PaidCardBuyRecordModifyRequest}
	 * @return 修改的付费会员信息 {@link PaidCardBuyRecordModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/modify")
	BaseResponse<PaidCardBuyRecordModifyResponse> modify(@RequestBody @Valid PaidCardBuyRecordModifyRequest paidCardBuyRecordModifyRequest);

	/**
	 * 单个删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordDelByIdRequest 单个删除参数结构 {@link PaidCardBuyRecordDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PaidCardBuyRecordDelByIdRequest paidCardBuyRecordDelByIdRequest);

	/**
	 * 批量删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardBuyRecordDelByIdListRequest 批量删除参数结构 {@link PaidCardBuyRecordDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardbuyrecord/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PaidCardBuyRecordDelByIdListRequest paidCardBuyRecordDelByIdListRequest);

}

