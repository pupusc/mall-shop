package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.enums.AccountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAccountNameExistsRequest implements Serializable {

    private static final long serialVersionUID = -7140240358915458802L;

    @ApiModelProperty(value = "账户名称")
    @NotNull
    private String accountName;

    @ApiModelProperty(value = "账户类型")
    @NotNull
    private AccountType accountType;
}
