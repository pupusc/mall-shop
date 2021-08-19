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
public class EmployeeLoginTimeModifyByIdRequest implements Serializable {

    private static final long serialVersionUID = -4385298890737553215L;
    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    @NotNull
    private String employeeId;
}
