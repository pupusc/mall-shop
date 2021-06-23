package com.wanmi.sbc.account.invoice;

import com.wanmi.sbc.account.api.constant.AccountErrorCode;
import com.wanmi.sbc.account.invoice.request.InvoiceProjectSwitchSaveRequest;
import com.wanmi.sbc.account.invoice.response.InvoiceProjectSwitchResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by chenli on 2017/12/12.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class InvoiceProjectSwitchService {

    @Autowired
    private InvoiceProjectSwitchRepository invoiceProjectSwitchRepository;

    /**
     * 根据商家id查询商家的开票类型
     * @param companyInfoId
     * @return
     */
    @Transactional
    public InvoiceProjectSwitch findBycompanyInfoId(Long companyInfoId){
        InvoiceProjectSwitch projectSwitch = invoiceProjectSwitchRepository.findByCompanyInfoId(companyInfoId);
        //如果没查到，则初始化一条
        if (Objects.isNull(projectSwitch)){
            InvoiceProjectSwitch newSwitch = new InvoiceProjectSwitch();
            newSwitch.setCompanyInfoId(companyInfoId);
            newSwitch.setIsSupportInvoice(DefaultFlag.NO);
            newSwitch.setIsPaperInvoice(DefaultFlag.NO);
            newSwitch.setIsValueAddedTaxInvoice(DefaultFlag.NO);
            return invoiceProjectSwitchRepository.saveAndFlush(newSwitch);
        }
        return projectSwitch;
    }

    /**
     * 根据商家ids批量查询商家的开票类型
     * @param companyInfoIds
     * @return
     */
    public  List<InvoiceProjectSwitch> findBycompanyInfoIds(List<Long> companyInfoIds){
        return invoiceProjectSwitchRepository.findSupportInvoice(companyInfoIds);
    }

    /**
     * 根据商家id集合，查询商家是否支持开票
     * @param companyInfoIds
     * @return
     */
    public List<InvoiceProjectSwitchResponse> findSupportInvoice(List<Long> companyInfoIds) {
        List<InvoiceProjectSwitch> switchList = invoiceProjectSwitchRepository.findSupportInvoice(companyInfoIds);
        return companyInfoIds.stream().map(id -> {
                    InvoiceProjectSwitchResponse response = new InvoiceProjectSwitchResponse();
                    response.setSupportInvoice(switchList.stream().filter(f -> Objects.equals(f.getCompanyInfoId(), id))
                            .findFirst().orElse(new InvoiceProjectSwitch()).getIsSupportInvoice());
                    response.setCompanyInfoId(id);
                    return response;
                }
        ).collect(Collectors.toList());
    }

    /**
     * 保存开票项目开关
     * @param saveRequest
     * @return
     */
    @Transactional
    public InvoiceProjectSwitch saveInvoiceSwitch(InvoiceProjectSwitchSaveRequest saveRequest) {
        InvoiceProjectSwitch projectSwitch = new InvoiceProjectSwitch();
        BeanUtils.copyProperties(saveRequest,projectSwitch);

        return invoiceProjectSwitchRepository.saveAndFlush(projectSwitch);
    }

    /**
     * 修改开票项目开关
     * @param saveRequest
     * @return
     */
    @Transactional
    public InvoiceProjectSwitch updateInvoiceSwitch(InvoiceProjectSwitchSaveRequest saveRequest) {
        //修改支持的开票类型
        InvoiceProjectSwitch projectSwitch = invoiceProjectSwitchRepository.findByCompanyInfoId(saveRequest.getCompanyInfoId());
        if (Objects.isNull(projectSwitch)){
            throw new SbcRuntimeException(AccountErrorCode.INVOICE_SWITCH_NOT_EXIST);
        }
        KsBeanUtil.copyProperties(saveRequest,projectSwitch);
        return invoiceProjectSwitchRepository.saveAndFlush(projectSwitch);
    }

}
