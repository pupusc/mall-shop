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
public class EmployeeJobNoExistsRequest implements Serializable {
    private static final long serialVersionUID = -1631237537817169853L;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String jobNo;

    /**
     * 账户类型
     */
    @ApiModelProperty(value = "账户类型")
    private AccountType accountType;

    /**
     * 公司id
     */
    private Long companyInfoId;
}
