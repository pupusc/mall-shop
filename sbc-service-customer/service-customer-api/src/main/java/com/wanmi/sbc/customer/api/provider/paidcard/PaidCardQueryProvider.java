package com.wanmi.sbc.customer.api.provider.paidcard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardBuyResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardPageResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardListResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardByIdResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员查询服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardQueryProvider")
public interface PaidCardQueryProvider {

	/**
	 * 分页查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardPageReq 分页请求参数和筛选对象 {@link PaidCardPageRequest}
	 * @return 付费会员分页列表信息 {@link PaidCardPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/page")
	BaseResponse<PaidCardPageResponse> page(@RequestBody @Valid PaidCardPageRequest paidCardPageReq);

	/**
	 * 列表查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardListReq 列表请求参数和筛选对象 {@link PaidCardListRequest}
	 * @return 付费会员的列表信息 {@link PaidCardListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/list")
	BaseResponse<PaidCardListResponse> list(@RequestBody @Valid PaidCardListRequest paidCardListReq);

	/**
	 * 单个查询付费会员API
	 *
	 * @author xuhai
	 * @param paidCardByIdRequest 单个查询付费会员请求参数 {@link PaidCardByIdRequest}
	 * @return 付费会员详情 {@link PaidCardByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/get-by-id")
	BaseResponse<PaidCardByIdResponse> getById(@RequestBody @Valid PaidCardByIdRequest paidCardByIdRequest);

	/**
	 * 用户端查询付费卡根据登录用户查询付费卡信息
	 * @param request
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/query-card-info")
	BaseResponse<List<PaidCardVO>> queryCardInfo(@RequestBody CustomerPaidCardQueryRequest request);


	/**
	 * 确认购买付费会员
	 * @param req
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/commit")
    BaseResponse<PaidCardBuyResponse> commit( @RequestBody @Valid PaidCardBuyRequest req);
}

