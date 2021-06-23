package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchProvider;
import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchAddRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchModifyRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchByCompanyInfoIdResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 开票项目类型管理
 * Created by chenli on 2017/12/12.
 */
@Api(tags = "InvoiceProjectSwitchController", description = "开票项目类型管理 API")
@RestController
@RequestMapping("/account")
public class InvoiceProjectSwitchController {

    @Autowired
    private InvoiceProjectSwitchQueryProvider invoiceProjectSwitchQueryProvider;

    @Autowired
    private InvoiceProjectSwitchProvider invoiceProjectSwitchProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 新增开票项目类型
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @ApiOperation(value = "新增开票项目类型")
    @RequestMapping(value = "/invoice/switch", method = RequestMethod.POST)
    public BaseResponse saveInvoiceProject(@RequestBody InvoiceProjectSwitchAddRequest invoiceProjectSaveRequest) {
        invoiceProjectSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        invoiceProjectSwitchProvider.add(invoiceProjectSaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 编辑开票项目类型
     *
     * @param invoiceProjectSaveRequest
     * @return
     */
    @ApiOperation(value = "编辑开票项目类型")
    @RequestMapping(value = "/invoice/switch", method = RequestMethod.PUT)
    public BaseResponse editInvoiceProject(@RequestBody InvoiceProjectSwitchModifyRequest invoiceProjectSaveRequest) {
        if (Objects.isNull(invoiceProjectSaveRequest.getInvoiceProjectSwitchId())){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        invoiceProjectSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        invoiceProjectSwitchProvider.modify(invoiceProjectSaveRequest);
        String message = "开票类型设为";
        if(invoiceProjectSaveRequest.getIsSupportInvoice().equals(DefaultFlag.YES)){
           if(invoiceProjectSaveRequest.getIsPaperInvoice().equals(DefaultFlag.YES)) {
               message += "  普通发票";
           }
            if(invoiceProjectSaveRequest.getIsValueAddedTaxInvoice().equals(DefaultFlag.YES)) {
                message += "  增值税专用发票";
            }
        }else{
            message += " 不支持开票";
        }
        operateLogMQUtil.convertAndSend("财务","设置开票类型",
                "设置开票类型："+ message);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询开票项目类型
     * @return
     */
    @ApiOperation(value = "查询开票项目类型")
    @RequestMapping(value = "/invoice/switch", method = RequestMethod.GET)
    public BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> findInvoiceProjectSwitch() {
        InvoiceProjectSwitchByCompanyInfoIdRequest request = new InvoiceProjectSwitchByCompanyInfoIdRequest();
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> baseResponse = invoiceProjectSwitchQueryProvider.getByCompanyInfoId(request);
        return baseResponse;
    }
}
