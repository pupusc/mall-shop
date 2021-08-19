package com.wanmi.sbc.account.api.provider.invoice;

import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchAddRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchModifyRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchAddResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchModifyResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>开票项目开关操作接口</p>
 * Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "InvoiceProjectSwitchProvider")
public interface InvoiceProjectSwitchProvider {

    /**
     * 保存开票项目开关
     * @param invoiceProjectSwitchAddRequest 新增开票项目开关的数据结构 {@link InvoiceProjectSwitchAddRequest}
     * @return 返回开票项目开关对象
     */
    @PostMapping("/account/${application.account.version}/invoice/project/switch/add")
    BaseResponse<InvoiceProjectSwitchAddResponse> add(@RequestBody @Valid InvoiceProjectSwitchAddRequest invoiceProjectSwitchAddRequest);

    /**
     * 修改开票项目开关
     * @param invoiceProjectSwitchModifyRequest 修改开票项目开关的数据结构 {@link InvoiceProjectSwitchModifyRequest}
     * @return 返回开票项目开关对象
     */
    @PostMapping("/account/${application.account.version}/invoice/project/switch/modify")
    BaseResponse<InvoiceProjectSwitchModifyResponse> modify(@RequestBody @Valid InvoiceProjectSwitchModifyRequest invoiceProjectSwitchModifyRequest);
}
