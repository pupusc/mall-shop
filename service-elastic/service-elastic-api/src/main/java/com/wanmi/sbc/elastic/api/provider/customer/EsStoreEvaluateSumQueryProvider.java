package com.wanmi.sbc.elastic.api.provider.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.EsStoreEvaluateSumPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsStoreEvaluateSumPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺评价查询服务Provider</p>
 * @author houshuai
 * @date 2020-12-03 10:59:09
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsStoreEvaluateSumQueryProvider")
public interface EsStoreEvaluateSumQueryProvider {

	/**
	 * 分页查询店铺评价API
	 *
	 * @author liutao
	 * @param storeEvaluateSumPageRequest 分页请求参数和筛选对象 {@link }
	 * @return 店铺评价分页列表信息 {@link EsStoreEvaluateSumPageResponse}
	 */
	@PostMapping("/elastic/${application.elastic.version}/storeevaluatesum/page")
	BaseResponse<EsStoreEvaluateSumPageResponse> page(@RequestBody @Valid EsStoreEvaluateSumPageRequest storeEvaluateSumPageRequest);


	/**
	 * 初始化店铺评价列表数据
	 * @param request
	 * @return
	 */
	@PostMapping("/elastic/${application.elastic.version}/storeevaluatesum/init")
	BaseResponse init(@RequestBody @Validated EsStoreEvaluateSumPageRequest request);


}