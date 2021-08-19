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
public class EmployeeLoginLockTimeModifyByIdRequest implements Serializable {


    private static final long serialVersionUID = 3956696094688149507L;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    @NotNull
    protected String employeeId;
}
