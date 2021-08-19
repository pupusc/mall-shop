package com.wanmi.sbc.customer.provider.impl.paidcardcustomerrel;

import com.wanmi.sbc.customer.api.request.customer.CustomerDeletePaidCardRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelDelByIdListRequest;
import com.wanmi.sbc.customer.paidcardcustomerrel.service.PaidCardCustomerRelService;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员保存服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@RestController
@Validated
public class PaidCardCustomerRelSaveController implements PaidCardCustomerRelSaveProvider {
	@Autowired
	private PaidCardCustomerRelService paidCardCustomerRelService;

	@Override
	public BaseResponse<PaidCardCustomerRelAddResponse> add(@RequestBody @Valid PaidCardCustomerRelAddRequest paidCardCustomerRelAddRequest) {
		PaidCardCustomerRel paidCardCustomerRel = new PaidCardCustomerRel();
		KsBeanUtil.copyPropertiesThird(paidCardCustomerRelAddRequest, paidCardCustomerRel);
		return BaseResponse.success(new PaidCardCustomerRelAddResponse(
				paidCardCustomerRelService.wrapperVo(paidCardCustomerRelService.add(paidCardCustomerRel))));
	}

	@Override
	public BaseResponse<PaidCardCustomerRelModifyResponse> modify(@RequestBody @Valid PaidCardCustomerRelModifyRequest paidCardCustomerRelModifyRequest) {
		PaidCardCustomerRel paidCardCustomerRel = new PaidCardCustomerRel();
		KsBeanUtil.copyPropertiesThird(paidCardCustomerRelModifyRequest, paidCardCustomerRel);
		return BaseResponse.success(new PaidCardCustomerRelModifyResponse(
				paidCardCustomerRelService.wrapperVo(paidCardCustomerRelService.modify(paidCardCustomerRel))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PaidCardCustomerRelDelByIdRequest paidCardCustomerRelDelByIdRequest) {
		paidCardCustomerRelService.deleteById(paidCardCustomerRelDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardCustomerRelDelByIdListRequest paidCardCustomerRelDelByIdListRequest) {
		paidCardCustomerRelService.deleteByIdList(paidCardCustomerRelDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteCustomerPaidCard( @RequestBody CustomerDeletePaidCardRequest request) {
		BaseResponse response = this.paidCardCustomerRelService.deleteCustomerPaidCard(request);
		return response;
	}

	@Override
	public BaseResponse changeSendMsgFlag(@RequestBody List<String> relIdList) {
		this.paidCardCustomerRelService.changeSendMsgFlag(relIdList);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse changeExpireSendMsgFlag(@RequestBody List<String> relIdList) {
		this.paidCardCustomerRelService.changeExpireSendMsgFlag(relIdList);
		return BaseResponse.SUCCESSFUL();
	}
}

