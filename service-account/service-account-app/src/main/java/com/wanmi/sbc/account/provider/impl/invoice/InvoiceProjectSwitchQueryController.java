package com.wanmi.sbc.account.provider.impl.invoice;

import com.wanmi.sbc.account.api.provider.invoice.InvoiceProjectSwitchQueryProvider;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListByCompanyInfoIdResponse;
import com.wanmi.sbc.account.api.response.invoice.InvoiceProjectSwitchListSupportByCompanyInfoIdResponse;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchSupportVO;
import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import com.wanmi.sbc.account.invoice.InvoiceProjectSwitch;
import com.wanmi.sbc.account.invoice.InvoiceProjectSwitchService;
import com.wanmi.sbc.account.invoice.response.InvoiceProjectSwitchResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/10/22 9:30
 * @Version: 1.0
 */
@RestController
@Validated
public class InvoiceProjectSwitchQueryController implements InvoiceProjectSwitchQueryProvider{

    @Autowired
    private InvoiceProjectSwitchService invoiceProjectSwitchService;

    /**
     * 根据商家id查询商家的开票类型
     * @param invoiceProjectSwitchByCompanyInfoIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectSwitchByCompanyInfoIdResponse> getByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchByCompanyInfoIdRequest invoiceProjectSwitchByCompanyInfoIdRequest) {
        InvoiceProjectSwitch invoiceProjectSwitch =invoiceProjectSwitchService.findBycompanyInfoId(invoiceProjectSwitchByCompanyInfoIdRequest.getCompanyInfoId());
        if (Objects.isNull(invoiceProjectSwitch)) {
            return BaseResponse.success(new InvoiceProjectSwitchByCompanyInfoIdResponse());
        }
        InvoiceProjectSwitchByCompanyInfoIdResponse response = new InvoiceProjectSwitchByCompanyInfoIdResponse();
        KsBeanUtil.copyPropertiesThird(invoiceProjectSwitch,response);
        return BaseResponse.success(response);
    }

    /**
     * 根据多个商家id批量查询商家的开票类型
     * @param invoiceProjectSwitchListByCompanyInfoIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectSwitchListByCompanyInfoIdResponse> listByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchListByCompanyInfoIdRequest invoiceProjectSwitchListByCompanyInfoIdRequest) {
        List<InvoiceProjectSwitch> invoiceProjectSwitchList = invoiceProjectSwitchService.findBycompanyInfoIds(invoiceProjectSwitchListByCompanyInfoIdRequest.getCompanyInfoIds());
        if (CollectionUtils.isEmpty(invoiceProjectSwitchList)) {
            return BaseResponse.success(new InvoiceProjectSwitchListByCompanyInfoIdResponse());
        }
        List<InvoiceProjectSwitchVO> invoiceProjectSwitchVOList = KsBeanUtil.convert(invoiceProjectSwitchList,InvoiceProjectSwitchVO.class);
        return BaseResponse.success(new InvoiceProjectSwitchListByCompanyInfoIdResponse(invoiceProjectSwitchVOList));
    }

    /**
     * 根据商家id集合，查询商家是否支持开票
     * @param invoiceProjectSwitchListSupportByCompanyInfoIdRequest
     * @return
     */
    @Override

    public BaseResponse<InvoiceProjectSwitchListSupportByCompanyInfoIdResponse> listSupportByCompanyInfoId(@RequestBody @Valid InvoiceProjectSwitchListSupportByCompanyInfoIdRequest invoiceProjectSwitchListSupportByCompanyInfoIdRequest) {
        List<InvoiceProjectSwitchResponse> invoiceProjectSwitchResponses = invoiceProjectSwitchService.findSupportInvoice(invoiceProjectSwitchListSupportByCompanyInfoIdRequest.getCompanyInfoIds());
        if (CollectionUtils.isEmpty(invoiceProjectSwitchResponses)) {
            return BaseResponse.success(new InvoiceProjectSwitchListSupportByCompanyInfoIdResponse());
        }
        List<InvoiceProjectSwitchSupportVO> invoiceProjectSwitchSupportVOList = KsBeanUtil.convert(invoiceProjectSwitchResponses,InvoiceProjectSwitchSupportVO.class);
        return BaseResponse.success(new InvoiceProjectSwitchListSupportByCompanyInfoIdResponse(invoiceProjectSwitchSupportVOList));
    }
}
