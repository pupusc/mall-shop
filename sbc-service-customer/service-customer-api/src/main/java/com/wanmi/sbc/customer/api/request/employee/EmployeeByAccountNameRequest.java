package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.enums.AccountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeByAccountNameRequest implements Serializable {

    private static final long serialVersionUID = -5060203299139278798L;

    @ApiModelProperty(value = "账户名称")
    @NotNull
    private String accountName;

    @ApiModelProperty(value = "账号类型")
    @NonNull
    private AccountType accountType;
}
