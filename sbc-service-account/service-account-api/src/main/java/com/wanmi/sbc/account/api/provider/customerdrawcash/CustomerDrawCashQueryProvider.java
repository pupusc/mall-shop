package com.wanmi.sbc.account.api.provider.customerdrawcash;

import com.wanmi.sbc.account.api.request.customerdrawcash.*;
import com.wanmi.sbc.account.api.response.customerdrawcash.*;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * <p>会员提现管理查询服务Provider</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerDrawCashQueryProvider")
public interface CustomerDrawCashQueryProvider {

	/**
	 * 分页查询会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashPageReq 分页请求参数和筛选对象 {@link CustomerDrawCashPageRequest}
	 * @return 会员提现管理分页列表信息 {@link CustomerDrawCashPageResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/page")
	BaseResponse<CustomerDrawCashPageResponse> page(@RequestBody @Valid CustomerDrawCashPageRequest customerDrawCashPageReq);


	/**
	 * 导出查询会员提现管理API
	 *
	 * @author of2975
	 * @param customerDrawCashExportRequest 导出请求参数和筛选对象 {@link CustomerDrawCashExportRequest}
	 * @return 会员提现管理导出信息 {@link CustomerDrawCashExportResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/export")
	BaseResponse<CustomerDrawCashExportResponse> export(@RequestBody @Valid CustomerDrawCashExportRequest customerDrawCashExportRequest);

	/**
	 * 列表查询会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashListReq 列表请求参数和筛选对象 {@link CustomerDrawCashListRequest}
	 * @return 会员提现管理的列表信息 {@link CustomerDrawCashListResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/list")
	BaseResponse<CustomerDrawCashListResponse> list(@RequestBody @Valid CustomerDrawCashListRequest customerDrawCashListReq);

	/**
	 * 单个查询会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashByIdRequest 单个查询会员提现管理请求参数 {@link CustomerDrawCashByIdRequest}
	 * @return 会员提现管理详情 {@link CustomerDrawCashByIdResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/get-by-id")
	BaseResponse<CustomerDrawCashByIdResponse> getById(@RequestBody @Valid CustomerDrawCashByIdRequest customerDrawCashByIdRequest);

	/**
	 * 统计某个会员当日提现金额
	 * @param request
	 * @return
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/count-draw-cash-sum")
	BaseResponse<BigDecimal> countDrawCashSum(@RequestBody CustomerDrawCashQueryRequest request);


    /**
     * 统计当前标签页
     * @return
     */
    @PostMapping("/account/${application.account.version}/customerdrawcash/count-draw-cash-tab-num")
    BaseResponse<CustomerDrawCashStatusResponse> countDrawCashTabNum();

}

