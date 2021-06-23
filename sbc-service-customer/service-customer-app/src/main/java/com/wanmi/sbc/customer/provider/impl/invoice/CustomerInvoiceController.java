package com.wanmi.sbc.customer.provider.impl.invoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.invoice.CustomerInvoiceProvider;
import com.wanmi.sbc.customer.api.request.invoice.*;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceAddResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceModifyResponse;
import com.wanmi.sbc.customer.invoice.model.entity.CustomerInvoiceSaveRequest;
import com.wanmi.sbc.customer.invoice.model.entity.InvoiceBatchRequest;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoice;
import com.wanmi.sbc.customer.invoice.service.CustomerInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/19 9:31
 * @Version: 1.0
 */
@Validated
@RestController
public class CustomerInvoiceController implements CustomerInvoiceProvider {

    @Autowired
    private CustomerInvoiceService customerInvoiceService;

    /**
     * 保存客户的增专票信息
     * boss端新增增票信息，增票状态都是已审核
     * 客户端新增增票信息，增票状态都是待审核
     * @param customerInvoiceAddRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceAddResponse> add(@RequestBody @Valid CustomerInvoiceAddRequest customerInvoiceAddRequest) {
        CustomerInvoiceSaveRequest request = new CustomerInvoiceSaveRequest();
        KsBeanUtil.copyPropertiesThird(customerInvoiceAddRequest,request);
        CustomerInvoice customerInvoice = customerInvoiceService.saveCustomerInvoice(request,customerInvoiceAddRequest.getEmployeeId()).orElse(null);
        CustomerInvoiceAddResponse customerInvoiceAddResponse = CustomerInvoiceAddResponse.builder().build();
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceAddResponse);
        return BaseResponse.success(customerInvoiceAddResponse);
    }

    /**
     * 修改客户的增专票信息
     * boss端修改增票信息，增票状态都是已审核
     * 客户端修改增票信息，增票状态都是待审核
     * @param customerInvoiceModifyRequest
     * @return
     */

    @Override
    public BaseResponse<CustomerInvoiceModifyResponse> modify(@RequestBody @Valid CustomerInvoiceModifyRequest customerInvoiceModifyRequest) {
        CustomerInvoiceSaveRequest request = new CustomerInvoiceSaveRequest();
        KsBeanUtil.copyPropertiesThird(customerInvoiceModifyRequest,request);
        CustomerInvoice customerInvoice = customerInvoiceService.updateCustomerInvoice(request,customerInvoiceModifyRequest.getEmployeeId());
        CustomerInvoiceModifyResponse customerInvoiceModifyResponse = CustomerInvoiceModifyResponse.builder().build();
        if (Objects.isNull(customerInvoice)){
            return BaseResponse.SUCCESSFUL();
        }
        KsBeanUtil.copyPropertiesThird(customerInvoice,customerInvoiceModifyResponse);
        return BaseResponse.success(customerInvoiceModifyResponse);
    }

    /**
     * 单条 / 批量审核 增专票信息
     * @param customerInvoiceAuditingRequest
     * @return
     */

    @Override
    public BaseResponse auditing(@RequestBody @Valid CustomerInvoiceAuditingRequest customerInvoiceAuditingRequest) {
        customerInvoiceService.checkCustomerInvoice(customerInvoiceAuditingRequest.getCheckState(),customerInvoiceAuditingRequest.getCustomerInvoiceIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回增专票信息
     * @param customerInvoiceRejectRequest
     * @return
     */

    @Override
    public BaseResponse reject(@RequestBody @Valid CustomerInvoiceRejectRequest customerInvoiceRejectRequest) {
        InvoiceBatchRequest invoiceBatchRequest = new InvoiceBatchRequest();
        KsBeanUtil.copyPropertiesThird(customerInvoiceRejectRequest, invoiceBatchRequest);
        customerInvoiceService.rejectInvoice(invoiceBatchRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 作废 增专票信息
     * @param customerInvoiceInvalidRequest
     * @return
     */

    @Override
    public BaseResponse invalid(@RequestBody @Valid CustomerInvoiceInvalidRequest customerInvoiceInvalidRequest) {
        customerInvoiceService.invalidCustomerInvoice(customerInvoiceInvalidRequest.getCustomerInvoiceIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除 增专票信息
     * @param customerInvoiceDeleteRequest
     * @return
     */

    @Override
    public BaseResponse delete(@RequestBody @Valid CustomerInvoiceDeleteRequest customerInvoiceDeleteRequest) {
        customerInvoiceService.deleteCustomerInvoice(customerInvoiceDeleteRequest.getCustomerInvoiceIds());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 保存增专资质配置
     * @param customerInvoiceConfigAddRequest
     * @return
     */

    @Override
    public BaseResponse addCustomerInvoiceConfig(@RequestBody @Valid CustomerInvoiceConfigAddRequest customerInvoiceConfigAddRequest) {
        customerInvoiceService.saveInvoiceConfig(customerInvoiceConfigAddRequest.getStatus());
        return BaseResponse.SUCCESSFUL();
    }
}
