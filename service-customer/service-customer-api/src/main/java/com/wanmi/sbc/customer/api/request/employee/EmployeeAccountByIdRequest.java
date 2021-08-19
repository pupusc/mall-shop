package com.wanmi.sbc.customer.api.request.employee;

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
public class EmployeeAccountByIdRequest implements Serializable {

    private static final long serialVersionUID = -91025417955796624L;

    @ApiModelProperty(value = "员工编号")
    @NotNull
    private String employeeId;
}
