package com.wanmi.sbc.account.invoice.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;

import java.io.Serializable;

/**
 * 开票项目开关返回结果
 * Created by chenli on 2017/12/12.
 */
@Data
public class InvoiceProjectSwitchResponse implements Serializable {

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 是否支持开票 0 不支持 1 支持
     */
    private DefaultFlag supportInvoice = DefaultFlag.NO;
}
