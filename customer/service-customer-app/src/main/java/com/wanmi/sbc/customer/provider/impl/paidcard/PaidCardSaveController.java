package com.wanmi.sbc.customer.provider.impl.paidcard;

import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleAddRequest;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import com.wanmi.sbc.customer.bean.enums.StatusEnum;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardSaveProvider;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardAddResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardModifyResponse;
import com.wanmi.sbc.customer.paidcard.service.PaidCardService;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员保存服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@RestController
@Validated
public class PaidCardSaveController implements PaidCardSaveProvider {
	@Autowired
	private PaidCardService paidCardService;

	@Override
	public BaseResponse<PaidCardAddResponse> add(@RequestBody @Valid PaidCardAddRequest paidCardAddRequest) {

		PaidCard paidCard = paidCardService.add(paidCardAddRequest);
		BaseResponse<PaidCardAddResponse> response = BaseResponse.success(new PaidCardAddResponse(
				paidCardService.wrapperVo(paidCard)));
		return response;
	}

	@Override
	public BaseResponse<PaidCardModifyResponse> modify(@RequestBody @Valid PaidCardModifyRequest paidCardModifyRequest) {
		PaidCard paidCard = new PaidCard();
		KsBeanUtil.copyPropertiesThird(paidCardModifyRequest, paidCard);
		return BaseResponse.success(new PaidCardModifyResponse(
				paidCardService.wrapperVo(paidCardService.modify(paidCardModifyRequest))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PaidCardDelByIdRequest paidCardDelByIdRequest) {
		paidCardService.deleteById(paidCardDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardDelByIdListRequest paidCardDelByIdListRequest) {
		paidCardService.deleteByIdList(paidCardDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse changeEnableStatus(@Valid @RequestBody PaidCardEnableRequest request) {
		this.paidCardService.changeEnableStatus(request.getId(),request.getEnable());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse dealPayCallBack( @RequestBody PaidCardRedisDTO paidCardRedisDTO) {
		this.paidCardService.dealPayCallBack(paidCardRedisDTO);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse sendWillExpireSms(@RequestBody List<PaidCardExpireRequest> requestList) {
		this.paidCardService.sendWillExpireSms(requestList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse sendExpireSms(@RequestBody List<PaidCardExpireRequest> requestList) {
		this.paidCardService.sendExpireSms(requestList);
		return BaseResponse.SUCCESSFUL();
	}
}

