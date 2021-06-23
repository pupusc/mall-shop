package com.wanmi.sbc.account.invoice.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 开票项目开关保存参数
 * Created by chenli on 2017/12/12.
 */
@Data
public class InvoiceProjectSwitchSaveRequest implements Serializable {

    /**
     * 主键
     */
    private String invoiceProjectSwitchId;

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 是否支持开票 0 支持 1 不支持
     */
    @Enumerated
    private DefaultFlag isSupportInvoice;

    /**
     * 纸质发票 0 支持 1 不支持
     */
    @Enumerated
    private DefaultFlag isPaperInvoice;

    /**
     * 增值税发票 0 支持 1 不支持
     */
    @Enumerated
    private DefaultFlag isValueAddedTaxInvoice;
}
