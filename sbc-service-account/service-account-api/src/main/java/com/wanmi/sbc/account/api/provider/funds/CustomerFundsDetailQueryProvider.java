package com.wanmi.sbc.account.api.provider.funds;

import com.wanmi.sbc.account.api.request.funds.CustomerFundsAmountRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailExportRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailModifyRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailPageRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsDetailExportResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsDetailPageResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsTodayResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员资金明细查询接口
 * @author: Geek Wang
 * @createDate: 2019/2/19 14:33
 * @version: 1.0
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerFundsDetailQueryProvider")
public interface CustomerFundsDetailQueryProvider {

    /**
     * 会员资金明细分页查询
     * @param request 分页查询条件 {@link CustomerFundsDetailPageRequest}
     * @return {@link BaseResponse<CustomerFundsDetailPageResponse>}
     */
    @PostMapping("/account/${application.account.version}/funds/detail/page")
    BaseResponse<CustomerFundsDetailPageResponse> page(@RequestBody @Valid CustomerFundsDetailPageRequest request);


    /**
     * 会员资金明细导出查询
     * @param request 导出查询条件 {@link CustomerFundsDetailExportRequest}
     * @return {@link BaseResponse<CustomerFundsDetailExportResponse>}
     */
    @PostMapping("/account/${application.account.version}/funds/detail/export")
    BaseResponse<CustomerFundsDetailExportResponse> export(@RequestBody @Valid CustomerFundsDetailExportRequest request);

    /**
     * 更新会员资金明细
     *
     * @return {@link BaseResponse<CustomerFundsDetailPageResponse>}
     */
    @PostMapping("/account/${application.account.version}/funds/detail/modify-customer-funds-detail")
    BaseResponse modifyCustomerFundsDetail(@RequestBody CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest);

    /**
     * 计算收支金额
     * @param request 查询条件 {@link CustomerFundsDetailPageRequest}
     * @return {@link BaseResponse<CustomerFundsTodayResponse>}
     */
    @PostMapping("/account/${application.account.version}/funds/detail/get-funds-change")
    BaseResponse<CustomerFundsTodayResponse> getFundsChange(@RequestBody @Valid CustomerFundsAmountRequest request);
}
