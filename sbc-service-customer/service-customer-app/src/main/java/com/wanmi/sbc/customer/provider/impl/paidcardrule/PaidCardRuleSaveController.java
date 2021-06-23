package com.wanmi.sbc.customer.provider.impl.paidcardrule;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardrule.PaidCardRuleSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleDelByIdListRequest;
import com.wanmi.sbc.customer.paidcardrule.service.PaidCardRuleService;
import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@RestController
@Validated
public class PaidCardRuleSaveController implements PaidCardRuleSaveProvider {
	@Autowired
	private PaidCardRuleService paidCardRuleService;

	@Override
	public BaseResponse<PaidCardRuleAddResponse> add(@RequestBody @Valid PaidCardRuleAddRequest paidCardRuleAddRequest) {
		PaidCardRule paidCardRule = new PaidCardRule();
		KsBeanUtil.copyPropertiesThird(paidCardRuleAddRequest, paidCardRule);
		return BaseResponse.success(new PaidCardRuleAddResponse(
				paidCardRuleService.wrapperVo(paidCardRuleService.add(paidCardRule))));
	}

	@Override
	public BaseResponse<PaidCardRuleModifyResponse> modify(@RequestBody @Valid PaidCardRuleModifyRequest paidCardRuleModifyRequest) {
		PaidCardRule paidCardRule = new PaidCardRule();
		KsBeanUtil.copyPropertiesThird(paidCardRuleModifyRequest, paidCardRule);
		return BaseResponse.success(new PaidCardRuleModifyResponse(
				paidCardRuleService.wrapperVo(paidCardRuleService.modify(paidCardRule))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PaidCardRuleDelByIdRequest paidCardRuleDelByIdRequest) {
		paidCardRuleService.deleteById(paidCardRuleDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRuleDelByIdListRequest paidCardRuleDelByIdListRequest) {
		paidCardRuleService.deleteByIdList(paidCardRuleDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

