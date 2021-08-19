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

/**
 * @author pengli
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeNameExistsRequest implements Serializable {

    private static final long serialVersionUID = 4922302058427806426L;

    @ApiModelProperty(value = "员工名称")
    @NotNull
    private String employeeName;

    @ApiModelProperty(value = "账号类型")
    @NotNull
    private AccountType accountType;
}
