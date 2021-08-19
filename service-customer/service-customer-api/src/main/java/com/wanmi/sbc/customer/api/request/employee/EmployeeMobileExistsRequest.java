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
public class EmployeeMobileExistsRequest implements Serializable {

    private static final long serialVersionUID = 7793624568279253673L;

    @ApiModelProperty(value = "员工手机号")
    @NotNull
    private String mobile;

    @ApiModelProperty(value = "账号类型")
    @NotNull
    private AccountType accountType;
}
