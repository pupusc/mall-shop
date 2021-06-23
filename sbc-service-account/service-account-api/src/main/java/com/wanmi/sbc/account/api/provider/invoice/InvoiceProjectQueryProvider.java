package com.wanmi.sbc.account.api.provider.invoice;

import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectByIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectPageByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectQueryRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectByIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectPageByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectQueryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 *  <p>开票项目查询接口</p>
 * Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "InvoiceProjectQueryProvider")
public interface InvoiceProjectQueryProvider {

    /**
     * 根据开票项目名称和公司ID查询该项目是否存在
     * @param invoiceProjectQueryRequest  包含开票项目名称和公司ID的数据结构  {@link InvoiceProjectQueryRequest}
     * @return 返回是否存在 true:是,false:否
     */
    @PostMapping("/account/${application.account.version}/invoice/project/query-by-project-name-and-company-info-id")
    BaseResponse<InvoiceProjectQueryResponse> queryByProjectNameAndCompanyInfoId(@RequestBody @Valid InvoiceProjectQueryRequest invoiceProjectQueryRequest);

    /**
     * 根据id查询开票项目
     * @param invoiceProjectByIdRequest 包含开票项目ID的数据结构  {@link InvoiceProjectByIdRequest}
     * @return 返回开票项目对象 {@link InvoiceProjectByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/get-by-id")
    BaseResponse<InvoiceProjectByIdResponse> getById(@RequestBody @Valid InvoiceProjectByIdRequest invoiceProjectByIdRequest);

    /**
     * 根据公司ID分页查询开票项目
     * @param invoiceProjectPageByCompanyInfoIdRequest 包含分页的条件查询数据结构  {@link InvoiceProjectPageByCompanyInfoIdRequest}
     * @return 开票项目分页列表 {@link InvoiceProjectPageByCompanyInfoIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/page-by-company-info-id")
    BaseResponse<InvoiceProjectPageByCompanyInfoIdResponse> pageByCompanyInfoId(@RequestBody @Valid InvoiceProjectPageByCompanyInfoIdRequest invoiceProjectPageByCompanyInfoIdRequest);

    /**
     * 根据公司ID查询所有开票项
     * @param invoiceProjectListByCompanyInfoIdRequest 包含公司信息Id查询数据结构  {@link InvoiceProjectListByCompanyInfoIdRequest}
     * @return 开票项目列表 {@link InvoiceProjectListByCompanyInfoIdResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/list-by-company-info-id")
    BaseResponse<InvoiceProjectListByCompanyInfoIdResponse> listByCompanyInfoId(@RequestBody @Valid InvoiceProjectListByCompanyInfoIdRequest invoiceProjectListByCompanyInfoIdRequest);
}
