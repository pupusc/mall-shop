package com.wanmi.sbc.customer.invoice.model.root;

import lombok.Data;

/**
 * Created by CHENLI on 2017/6/5.
 */
@Data
public class CustomerInvoiceInfoResponse {
    /**
     * 是否有增票资质
     */
    private boolean flag = Boolean.FALSE;

    /**
     * 是否支持增票资质
     */
    private boolean configFlag = Boolean.FALSE;

    /**
     * 是否支持纸质发票
     */
    private boolean paperInvoice = Boolean.FALSE;

    /**
     * 是否支持开票 pc端使用
     */
    private boolean support = Boolean.FALSE;

    /**
     * 商家Id
     */
    private Long companyInfoId;

    /**
     * 增票资质信息
     */
    private CustomerInvoiceResponse customerInvoiceResponse;
}
