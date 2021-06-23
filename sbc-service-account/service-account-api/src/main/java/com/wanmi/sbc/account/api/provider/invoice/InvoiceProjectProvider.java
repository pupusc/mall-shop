package com.wanmi.sbc.account.api.provider.invoice;

import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectAddRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectDeleteByIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectModifyRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectAddResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectModifyResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 *  <p>开票项目操作接口</p>
 *  Created by wanggang on 2018-10-12-上午10:30.
 */
@FeignClient(value = "${application.account.name}", contextId = "InvoiceProjectProvider")
public interface InvoiceProjectProvider {

    /**
     * 保存新增开票项目
     * @param invoiceProjectAddRequest 新增开票项目数据结构 {@link InvoiceProjectAddRequest}
     * @return 返回开票项目对象 {@link InvoiceProjectAddResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/add")
    BaseResponse<InvoiceProjectAddResponse> add(@RequestBody @Valid InvoiceProjectAddRequest invoiceProjectAddRequest);

    /**
     * 编辑开票项目
     * @param invoiceProjectModifyRequest 编辑开票项目数据结构 {@link InvoiceProjectModifyRequest}
     * @return 返回开票项目对象 {@link InvoiceProjectModifyResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/modify")
    BaseResponse<InvoiceProjectModifyResponse> modify(@RequestBody @Valid InvoiceProjectModifyRequest invoiceProjectModifyRequest);

    /**
     * 根据ID删除开票项目
     * @param invoiceProjectDeleteByIdRequest 包含开票项目Id的数据结构 {@link InvoiceProjectDeleteByIdRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/invoice/project/delete")
    BaseResponse delete(@RequestBody @Valid InvoiceProjectDeleteByIdRequest invoiceProjectDeleteByIdRequest);
}
