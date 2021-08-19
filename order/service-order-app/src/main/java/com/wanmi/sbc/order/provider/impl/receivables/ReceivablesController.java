package com.wanmi.sbc.order.provider.impl.receivables;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.receivables.ReceivableProvider;
import com.wanmi.sbc.order.api.request.receivables.ReceivablesDeleteRequests;
import com.wanmi.sbc.order.receivables.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class ReceivablesController implements ReceivableProvider {


    @Autowired
    private ReceivableService service;

    @Override
    public BaseResponse deleteReceivables(ReceivablesDeleteRequests id) {

        service.deleteReceivables(id.getContent());

        return BaseResponse.success(id);
    }
}
