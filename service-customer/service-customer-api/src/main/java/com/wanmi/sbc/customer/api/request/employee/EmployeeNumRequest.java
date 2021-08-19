package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.enums.AccountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeNumRequest implements Serializable {
    private static final long serialVersionUID = 4406541696456189248L;

    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id")
    private Long companyInfoId;

    /**
     * 账户类型
     */
    @ApiModelProperty(value = "账户类型")
    private AccountType accountType;
}
