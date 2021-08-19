package com.wanmi.sbc.customer.api.provider.invoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceAddResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员增票资质-增加/修改/删除/审核/驳回/作废API
 * @Author: wanggang
 * @CreateDate: 2018/9/18 10:20
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerInvoiceProvider")
public interface CustomerInvoiceProvider {

    /**
     * 保存客户的增专票信息
     * boss端新增增票信息，增票状态都是已审核
     * 客户端新增增票信息，增票状态都是待审核
     *
     * @param customerInvoiceAddRequest {@link CustomerInvoiceAddRequest}
     * @return 新增专票结果 {@link CustomerInvoiceAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/add")
    BaseResponse<CustomerInvoiceAddResponse> add(@RequestBody @Valid CustomerInvoiceAddRequest
                                                         customerInvoiceAddRequest);

    /**
     * 修改客户的增专票信息
     * boss端修改增票信息，增票状态都是已审核
     * 客户端修改增票信息，增票状态都是待审核
     *
     * @param customerInvoiceModifyRequest {@link CustomerInvoiceModifyRequest}
     * @return 修改专票结果 {@link CustomerInvoiceModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/modify")
    BaseResponse<CustomerInvoiceModifyResponse> modify(@RequestBody @Valid CustomerInvoiceModifyRequest customerInvoiceModifyRequest);

    /**
     * 单条 / 批量审核 增专票信息
     *
     * @param customerInvoiceAuditingRequest {@link CustomerInvoiceAuditingRequest}
     * @return 审核专票结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/auditing")
    BaseResponse auditing(@RequestBody @Valid CustomerInvoiceAuditingRequest customerInvoiceAuditingRequest);

    /**
     * 驳回增专票信息
     *
     * @param invoiceBatchRequest {@link CustomerInvoiceRejectRequest}
     * @return 驳回专票结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/reject")
    BaseResponse reject(@RequestBody @Valid CustomerInvoiceRejectRequest invoiceBatchRequest);

    /**
     * 作废 增专票信息
     *
     * @param customerInvoiceInvalidRequest {@link CustomerInvoiceInvalidRequest}
     * @return 作废专票结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/invalid")
    BaseResponse invalid(@RequestBody @Valid CustomerInvoiceInvalidRequest customerInvoiceInvalidRequest);

    /**
     * 删除 增专票信息
     *
     * @param customerInvoiceDeleteRequest {@link CustomerInvoiceDeleteRequest}
     * @return 删除专票结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/delete")
    BaseResponse delete(@RequestBody @Valid CustomerInvoiceDeleteRequest customerInvoiceDeleteRequest);

    /**
     * 保存增专资质配置
     *
     * @param customerInvoiceConfigAddRequest {@link CustomerInvoiceConfigAddRequest}
     * @return 保存专票资质结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/invoice/add-customer-invoice-config")
    BaseResponse addCustomerInvoiceConfig(@RequestBody @Valid CustomerInvoiceConfigAddRequest customerInvoiceConfigAddRequest);
}
