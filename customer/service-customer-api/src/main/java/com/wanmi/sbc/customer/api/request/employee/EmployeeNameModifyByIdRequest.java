package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * com.wanmi.sbc.customer.api.request.employee.EmployeeNameModifyRequest
 *
 * @author lipeng
 * @dateTime 2018/9/11 上午9:43
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeNameModifyByIdRequest implements Serializable {

    private static final long serialVersionUID = 7321223288786312920L;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    @NotNull
    protected String employeeId;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

}
