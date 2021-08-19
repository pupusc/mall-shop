package com.wanmi.sbc.customer.provider.impl.paidcardrightsrel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardrightsrel.PaidCardRightsRelSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelDelByIdListRequest;
import com.wanmi.sbc.customer.paidcardrightsrel.service.PaidCardRightsRelService;
import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@RestController
@Validated
public class PaidCardRightsRelSaveController implements PaidCardRightsRelSaveProvider {
	@Autowired
	private PaidCardRightsRelService paidCardRightsRelService;

	@Override
	public BaseResponse<PaidCardRightsRelAddResponse> add(@RequestBody @Valid PaidCardRightsRelAddRequest paidCardRightsRelAddRequest) {
		PaidCardRightsRel paidCardRightsRel = new PaidCardRightsRel();
		KsBeanUtil.copyPropertiesThird(paidCardRightsRelAddRequest, paidCardRightsRel);
		return BaseResponse.success(new PaidCardRightsRelAddResponse(
				paidCardRightsRelService.wrapperVo(paidCardRightsRelService.add(paidCardRightsRel))));
	}

	@Override
	public BaseResponse<PaidCardRightsRelModifyResponse> modify(@RequestBody @Valid PaidCardRightsRelModifyRequest paidCardRightsRelModifyRequest) {
		PaidCardRightsRel paidCardRightsRel = new PaidCardRightsRel();
		KsBeanUtil.copyPropertiesThird(paidCardRightsRelModifyRequest, paidCardRightsRel);
		return BaseResponse.success(new PaidCardRightsRelModifyResponse(
				paidCardRightsRelService.wrapperVo(paidCardRightsRelService.modify(paidCardRightsRel))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PaidCardRightsRelDelByIdRequest paidCardRightsRelDelByIdRequest) {
		paidCardRightsRelService.deleteById(paidCardRightsRelDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRightsRelDelByIdListRequest paidCardRightsRelDelByIdListRequest) {
		paidCardRightsRelService.deleteByIdList(paidCardRightsRelDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

