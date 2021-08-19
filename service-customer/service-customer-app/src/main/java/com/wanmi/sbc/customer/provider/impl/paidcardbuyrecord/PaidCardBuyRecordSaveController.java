package com.wanmi.sbc.customer.provider.impl.paidcardbuyrecord;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordSaveProvider;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardbuyrecord.PaidCardBuyRecordModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordDelByIdListRequest;
import com.wanmi.sbc.customer.paidcardbuyrecord.service.PaidCardBuyRecordService;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务接口实现</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@RestController
@Validated
public class PaidCardBuyRecordSaveController implements PaidCardBuyRecordSaveProvider {
	@Autowired
	private PaidCardBuyRecordService paidCardBuyRecordService;

	@Override
	public BaseResponse<PaidCardBuyRecordAddResponse> add(@RequestBody @Valid PaidCardBuyRecordAddRequest paidCardBuyRecordAddRequest) {
		PaidCardBuyRecord paidCardBuyRecord = new PaidCardBuyRecord();
		KsBeanUtil.copyPropertiesThird(paidCardBuyRecordAddRequest, paidCardBuyRecord);
		return BaseResponse.success(new PaidCardBuyRecordAddResponse(
				paidCardBuyRecordService.wrapperVo(paidCardBuyRecordService.add(paidCardBuyRecord))));
	}

	@Override
	public BaseResponse<PaidCardBuyRecordModifyResponse> modify(@RequestBody @Valid PaidCardBuyRecordModifyRequest paidCardBuyRecordModifyRequest) {
		PaidCardBuyRecord paidCardBuyRecord = new PaidCardBuyRecord();
		KsBeanUtil.copyPropertiesThird(paidCardBuyRecordModifyRequest, paidCardBuyRecord);
		return BaseResponse.success(new PaidCardBuyRecordModifyResponse(
				paidCardBuyRecordService.wrapperVo(paidCardBuyRecordService.modify(paidCardBuyRecord))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PaidCardBuyRecordDelByIdRequest paidCardBuyRecordDelByIdRequest) {
		paidCardBuyRecordService.deleteById(paidCardBuyRecordDelByIdRequest.getPayCode());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PaidCardBuyRecordDelByIdListRequest paidCardBuyRecordDelByIdListRequest) {
		paidCardBuyRecordService.deleteByIdList(paidCardBuyRecordDelByIdListRequest.getPayCodeList());
		return BaseResponse.SUCCESSFUL();
	}

}

