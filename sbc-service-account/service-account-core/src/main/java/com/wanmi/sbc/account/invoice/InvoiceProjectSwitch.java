package com.wanmi.sbc.account.invoice;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 开票项目开关
 * Created by chenli on 2017/12/12.
 */
@Data
@Entity
@Table(name = "invoice_project_switch")
public class InvoiceProjectSwitch implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_project_switch_id")
    private String invoiceProjectSwitchId;

    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 是否支持开票 0 不支持 1 支持
     */
    @Column(name = "is_support_invoice")
    @Enumerated
    private DefaultFlag isSupportInvoice = DefaultFlag.NO;

    /**
     * 纸质发票 0 不支持 1 支持
     */
    @Column(name = "is_paper_invoice")
    @Enumerated
    private DefaultFlag isPaperInvoice = DefaultFlag.NO;

    /**
     * 增值税发票 0 不支持 1 支持
     */
    @Column(name = "is_value_added_tax_invoice")
    @Enumerated
    private DefaultFlag isValueAddedTaxInvoice = DefaultFlag.NO;
}
