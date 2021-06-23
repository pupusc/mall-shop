package com.wanmi.sbc.account.provider.impl.invoice;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectAddRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectDeleteByIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectModifyRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectAddResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectModifyResponse;
import com.wanmi.sbc.account.invoice.InvoiceProject;
import com.wanmi.sbc.account.invoice.InvoiceProjectService;
import com.wanmi.sbc.account.invoice.request.InvoiceProjectSaveRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: wanggang
 * @CreateDate: 2018/10/22 9:30
 * @Version: 1.0
 */
@RestController
@Validated
public class InvoiceProjectController implements InvoiceProjectProvider {

    @Autowired
    private InvoiceProjectService invoiceProjectService;

    /**
     * 保存新增开票项目
     * @param invoiceProjectAddRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectAddResponse> add(@RequestBody @Valid InvoiceProjectAddRequest invoiceProjectAddRequest) {
        InvoiceProjectSaveRequest invoiceProjectSaveRequest = new InvoiceProjectSaveRequest();
        KsBeanUtil.copyPropertiesThird(invoiceProjectAddRequest,invoiceProjectSaveRequest);
        InvoiceProject invoiceProject = invoiceProjectService.saveInvoiceProject(invoiceProjectSaveRequest);
        InvoiceProjectAddResponse invoiceProjectAddResponse = new InvoiceProjectAddResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProject,invoiceProjectAddResponse);
        return BaseResponse.success(invoiceProjectAddResponse);
    }

    /**
     * 编辑开票项目
     * @param invoiceProjectModifyRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectModifyResponse> modify(@RequestBody @Valid InvoiceProjectModifyRequest invoiceProjectModifyRequest) {
        InvoiceProjectSaveRequest invoiceProjectSaveRequest = new InvoiceProjectSaveRequest();
        KsBeanUtil.copyPropertiesThird(invoiceProjectModifyRequest,invoiceProjectSaveRequest);
        InvoiceProject invoiceProject = invoiceProjectService.editInvoiceProject(invoiceProjectSaveRequest);
        InvoiceProjectModifyResponse invoiceProjectModifyResponse = new InvoiceProjectModifyResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProject,invoiceProjectModifyResponse);
        return BaseResponse.success(invoiceProjectModifyResponse);
    }

    /**
     * 根据ID删除开票项目
     * @param invoiceProjectDeleteByIdRequest
     * @return
     */
    @Override

    public BaseResponse delete(@RequestBody @Valid InvoiceProjectDeleteByIdRequest invoiceProjectDeleteByIdRequest) {
        invoiceProjectService.deleteInvoiceProject(invoiceProjectDeleteByIdRequest.getProjectId());
        return BaseResponse.SUCCESSFUL();
    }
}
