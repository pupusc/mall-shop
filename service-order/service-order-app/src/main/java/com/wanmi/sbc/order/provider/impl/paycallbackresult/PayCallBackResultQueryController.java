package com.wanmi.sbc.order.provider.impl.paycallbackresult;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.paycallbackresult.PayCallBackResultQueryProvider;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultByIdRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultListRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultPageRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultQueryRequest;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultByIdResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultListResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultPageResponse;
import com.wanmi.sbc.order.bean.vo.PayCallBackResultVO;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.service.PayCallBackResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>支付回调结果查询服务接口实现</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@RestController
@Validated
public class PayCallBackResultQueryController implements PayCallBackResultQueryProvider {
	@Autowired
	private PayCallBackResultService payCallBackResultService;

	@Override
	public BaseResponse<PayCallBackResultPageResponse> page(@RequestBody @Valid PayCallBackResultPageRequest payCallBackResultPageReq) {
		PayCallBackResultQueryRequest queryReq = KsBeanUtil.convert(payCallBackResultPageReq, PayCallBackResultQueryRequest.class);
		Page<PayCallBackResult> payCallBackResultPage = payCallBackResultService.page(queryReq);
		Page<PayCallBackResultVO> newPage = payCallBackResultPage.map(entity -> payCallBackResultService.wrapperVo(entity));
		MicroServicePage<PayCallBackResultVO> microPage = new MicroServicePage<>(newPage, payCallBackResultPageReq.getPageable());
		PayCallBackResultPageResponse finalRes = new PayCallBackResultPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PayCallBackResultListResponse> list(@RequestBody @Valid PayCallBackResultListRequest payCallBackResultListReq) {
		PayCallBackResultQueryRequest queryReq = KsBeanUtil.convert(payCallBackResultListReq, PayCallBackResultQueryRequest.class);
		List<PayCallBackResult> payCallBackResultList = payCallBackResultService.list(queryReq);
		List<PayCallBackResultVO> newList = payCallBackResultList.stream().map(entity -> payCallBackResultService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PayCallBackResultListResponse(newList));
	}

	@Override
	public BaseResponse<PayCallBackResultByIdResponse> getById(@RequestBody @Valid PayCallBackResultByIdRequest payCallBackResultByIdRequest) {
		PayCallBackResult payCallBackResult =
		payCallBackResultService.getOne(payCallBackResultByIdRequest.getId());
		return BaseResponse.success(new PayCallBackResultByIdResponse(payCallBackResultService.wrapperVo(payCallBackResult)));
	}

}

