package com.wanmi.sbc.order.provider.impl.paycallbackresult;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.paycallbackresult.PayCallBackResultProvider;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultAddRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultDelByIdRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultModifyRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultModifyResultStatusRequest;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultAddResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultModifyResponse;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.service.PayCallBackResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>支付回调结果保存服务接口实现</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@RestController
@Validated
public class PayCallBackResultController implements PayCallBackResultProvider {
	@Autowired
	private PayCallBackResultService payCallBackResultService;

	@Override
	public BaseResponse<PayCallBackResultAddResponse> add(@RequestBody @Valid PayCallBackResultAddRequest payCallBackResultAddRequest) {
		PayCallBackResult payCallBackResult = KsBeanUtil.convert(payCallBackResultAddRequest, PayCallBackResult.class);
		return BaseResponse.success(new PayCallBackResultAddResponse(
				payCallBackResultService.wrapperVo(payCallBackResultService.add(payCallBackResult))));
	}

	@Override
	public BaseResponse<PayCallBackResultModifyResponse> modify(@RequestBody @Valid PayCallBackResultModifyRequest payCallBackResultModifyRequest) {
		PayCallBackResult payCallBackResult = KsBeanUtil.convert(payCallBackResultModifyRequest, PayCallBackResult.class);
		return BaseResponse.success(new PayCallBackResultModifyResponse(
				payCallBackResultService.wrapperVo(payCallBackResultService.modify(payCallBackResult))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PayCallBackResultDelByIdRequest payCallBackResultDelByIdRequest) {
		payCallBackResultService.deleteById(payCallBackResultDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modifyResultStatusByBusinessId(@Valid PayCallBackResultModifyResultStatusRequest payCallBackResultModifyResultStatusRequest) {
		payCallBackResultService.updateStatus(payCallBackResultModifyResultStatusRequest.getBusinessId(),payCallBackResultModifyResultStatusRequest.getResultStatus());
		return BaseResponse.SUCCESSFUL();
	}

}

