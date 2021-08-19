package com.wanmi.sbc.account.provider.impl.invoice;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchAddRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchModifyRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchAddResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchModifyResponse;
import com.wanmi.sbc.account.invoice.InvoiceProjectSwitch;
import com.wanmi.sbc.account.invoice.InvoiceProjectSwitchService;
import com.wanmi.sbc.account.invoice.request.InvoiceProjectSwitchSaveRequest;
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
public class InvoiceProjectSwitchController implements InvoiceProjectSwitchProvider {

    @Autowired
    private InvoiceProjectSwitchService invoiceProjectSwitchService;

    /**
     * 保存开票项目开关
     * @param invoiceProjectSwitchAddRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectSwitchAddResponse> add(@RequestBody @Valid InvoiceProjectSwitchAddRequest invoiceProjectSwitchAddRequest) {
        InvoiceProjectSwitchSaveRequest invoiceProjectSwitchSaveRequest = new InvoiceProjectSwitchSaveRequest();
        KsBeanUtil.copyPropertiesThird(invoiceProjectSwitchAddRequest,invoiceProjectSwitchSaveRequest);
        InvoiceProjectSwitch invoiceProjectSwitch = invoiceProjectSwitchService.saveInvoiceSwitch(invoiceProjectSwitchSaveRequest);
        InvoiceProjectSwitchAddResponse response = new InvoiceProjectSwitchAddResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProjectSwitch,response);
        return BaseResponse.success(response);
    }

    /**
     * 修改开票项目开关
     * @param invoiceProjectSwitchModifyRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectSwitchModifyResponse> modify(@RequestBody @Valid InvoiceProjectSwitchModifyRequest invoiceProjectSwitchModifyRequest) {
        InvoiceProjectSwitchSaveRequest invoiceProjectSwitchSaveRequest = new InvoiceProjectSwitchSaveRequest();
        KsBeanUtil.copyPropertiesThird(invoiceProjectSwitchModifyRequest,invoiceProjectSwitchSaveRequest);
        InvoiceProjectSwitch invoiceProjectSwitch = invoiceProjectSwitchService.updateInvoiceSwitch(invoiceProjectSwitchSaveRequest);
        InvoiceProjectSwitchModifyResponse response = new InvoiceProjectSwitchModifyResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProjectSwitch,response);
        return BaseResponse.success(response);
    }
}
