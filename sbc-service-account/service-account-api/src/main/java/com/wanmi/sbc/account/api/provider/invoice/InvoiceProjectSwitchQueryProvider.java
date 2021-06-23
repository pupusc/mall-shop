package com.wanmi.sbc.account.api.provider.invoice;

import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>开票项目开关查询接口</p>
 * Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "InvoiceProjectSwitchQueryProvider")
public interface InvoiceProjectSwitchQueryProvider {

    /**
     * 根据商家id查询商家的开票类型
     * @param invoiceProjectSwitchByCompanyInfoIdRequest 包含公司信息Id的查询数据结构 {@link InvoiceProjectSwitchByCompanyInfoIdRequest}
     * @return 开票项目开关 {@link InvoiceProjectSwitchByCompanyInfoIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/switch/get-by-company-info-id")
    BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> getByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchByCompanyInfoIdRequest invoiceProjectSwitchByCompanyInfoIdRequest);

    /**
     * 根据多个商家id批量查询商家的开票类型
     * @param invoiceProjectSwitchListByCompanyInfoIdRequest  包含公司信息Id的查询数据结构 {@link InvoiceProjectSwitchListByCompanyInfoIdRequest}
     * @return 开票项目开关列表 {@link InvoiceProjectSwitchListByCompanyInfoIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/switch/list-by-company-info-id")
    BaseResponse<InvoiceProjectSwitchListByCompanyInfoIdResponse> listByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchListByCompanyInfoIdRequest invoiceProjectSwitchListByCompanyInfoIdRequest);

    /**
     * 根据商家id集合，查询商家是否支持开票
     * @param invoiceProjectSwitchListSupportByCompanyInfoIdRequest 包含公司信息Id的查询数据结构 {@link InvoiceProjectSwitchListSupportByCompanyInfoIdRequest}
     * @return 包含是否支持开票的开票项目开关 {@link InvoiceProjectSwitchListSupportByCompanyInfoIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/switch/list-support-by-company-info-id")
    BaseResponse<InvoiceProjectSwitchListSupportByCompanyInfoIdResponse> listSupportByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchListSupportByCompanyInfoIdRequest invoiceProjectSwitchListSupportByCompanyInfoIdRequest);

}
