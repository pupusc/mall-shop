package com.wanmi.sbc.customer.provider.impl.paidcardrule;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardrule.PaidCardRuleQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRulePageRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleQueryRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRulePageResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleListRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleListResponse;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleByIdRequest;
import com.wanmi.sbc.customer.api.response.paidcardrule.PaidCardRuleByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardRuleVO;
import com.wanmi.sbc.customer.paidcardrule.service.PaidCardRuleService;
import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>付费会员查询服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@RestController
@Validated
public class PaidCardRuleQueryController implements PaidCardRuleQueryProvider {
	@Autowired
	private PaidCardRuleService paidCardRuleService;

	@Override
	public BaseResponse<PaidCardRulePageResponse> page(@RequestBody @Valid PaidCardRulePageRequest paidCardRulePageReq) {
		PaidCardRuleQueryRequest queryReq = new PaidCardRuleQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardRulePageReq, queryReq);
		Page<PaidCardRule> paidCardRulePage = paidCardRuleService.page(queryReq);
		Page<PaidCardRuleVO> newPage = paidCardRulePage.map(entity -> paidCardRuleService.wrapperVo(entity));
		MicroServicePage<PaidCardRuleVO> microPage = new MicroServicePage<>(newPage, paidCardRulePageReq.getPageable());
		PaidCardRulePageResponse finalRes = new PaidCardRulePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PaidCardRuleListResponse> list(@RequestBody @Valid PaidCardRuleListRequest paidCardRuleListReq) {
		PaidCardRuleQueryRequest queryReq = new PaidCardRuleQueryRequest();
		KsBeanUtil.copyPropertiesThird(paidCardRuleListReq, queryReq);
		List<PaidCardRule> paidCardRuleList = paidCardRuleService.list(queryReq);
		List<PaidCardRuleVO> newList = paidCardRuleList.stream().map(entity -> paidCardRuleService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PaidCardRuleListResponse(newList));
	}

	@Override
	public BaseResponse<PaidCardRuleByIdResponse> getById(@RequestBody @Valid PaidCardRuleByIdRequest paidCardRuleByIdRequest) {
		PaidCardRule paidCardRule = paidCardRuleService.getById(paidCardRuleByIdRequest.getId());
		return BaseResponse.success(new PaidCardRuleByIdResponse(paidCardRuleService.wrapperVo(paidCardRule)));
	}

	@Override
	public BaseResponse<PaidCardRuleByIdResponse> getByIdWithPaidCardInfo(@Valid @RequestBody PaidCardRuleByIdRequest paidCardRuleByIdRequest) {
		PaidCardRuleVO paidCardRule = paidCardRuleService.getByIdWithPaidCardInfo(paidCardRuleByIdRequest.getId());
		return BaseResponse.success(new PaidCardRuleByIdResponse(paidCardRule));

	}
}

