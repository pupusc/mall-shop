package com.wanmi.sbc.customer.request;

import com.wanmi.sbc.common.enums.AccountType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EmployeeExcelImportRequest implements Serializable {
    private static final long serialVersionUID = -5361113326901978426L;

    @NotBlank
    private String ext;

    /**
     * 公司ID
     */
    private Long companyInfoId;

    /**
     * 操作员id
     */
    private String userId;

    /**
     * 账户类型
     */
    private AccountType accountType;
}
