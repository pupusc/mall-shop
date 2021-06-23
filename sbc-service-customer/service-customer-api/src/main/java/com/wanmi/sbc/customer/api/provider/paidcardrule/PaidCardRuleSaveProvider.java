package com.wanmi.sbc.customer.api.provider.paidcardrule;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardRuleSaveProvider")
public interface PaidCardRuleSaveProvider {

	/**
	 * 新增付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleAddRequest 付费会员新增参数结构 {@link PaidCardRuleAddRequest}
	 * @return 新增的付费会员信息 {@link PaidCardRuleAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/add")
	BaseResponse<PaidCardRuleAddResponse> add(@RequestBody @Valid PaidCardRuleAddRequest paidCardRuleAddRequest);

	/**
	 * 修改付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleModifyRequest 付费会员修改参数结构 {@link PaidCardRuleModifyRequest}
	 * @return 修改的付费会员信息 {@link PaidCardRuleModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/modify")
	BaseResponse<PaidCardRuleModifyResponse> modify(@RequestBody @Valid PaidCardRuleModifyRequest paidCardRuleModifyRequest);

	/**
	 * 单个删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleDelByIdRequest 单个删除参数结构 {@link PaidCardRuleDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PaidCardRuleDelByIdRequest paidCardRuleDelByIdRequest);

	/**
	 * 批量删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRuleDelByIdListRequest 批量删除参数结构 {@link PaidCardRuleDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrule/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRuleDelByIdListRequest paidCardRuleDelByIdListRequest);

}

