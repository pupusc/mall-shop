package com.wanmi.sbc.customer.api.request.fandeng;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 发票推送接口
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanDengFullInvoiceRequest extends FanDengInvoiceRequest{

    private String orderCodes;
    private Integer receiptType;
    private Integer headerType;
    private String receiptHeader;
    private String content="1";
    private String taxCode;
    private String email;
    private boolean sendOrderFlag=true;
    private BigDecimal fee;
    private String companyAddress;
    private String companyPhone;
    private String companyBank;
    private String companyBankCard;
    private String remark;
    private String source = "1";
}
