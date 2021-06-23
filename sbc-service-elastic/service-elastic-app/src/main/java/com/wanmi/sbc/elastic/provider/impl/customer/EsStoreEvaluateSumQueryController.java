package com.wanmi.sbc.elastic.provider.impl.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.customer.EsStoreEvaluateSumQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsStoreEvaluateSumPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsStoreEvaluateSumPageResponse;
import com.wanmi.sbc.elastic.customer.service.EsStoreEvaluateSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>店铺评价查询服务Provider</p>
 * @author houshuai
 * @date 2020-12-03 10:59:09
 */
@RestController
public class EsStoreEvaluateSumQueryController implements EsStoreEvaluateSumQueryProvider {

	@Autowired
	private EsStoreEvaluateSumService storeEvaluateSumService;


	@Override
	public BaseResponse<EsStoreEvaluateSumPageResponse> page(@RequestBody @Valid EsStoreEvaluateSumPageRequest storeEvaluateSumPageRequest) {
		return storeEvaluateSumService.page(storeEvaluateSumPageRequest);
	}

	@Override
	public BaseResponse init(EsStoreEvaluateSumPageRequest request) {
		storeEvaluateSumService.init(request);
		return BaseResponse.SUCCESSFUL();
	}
}