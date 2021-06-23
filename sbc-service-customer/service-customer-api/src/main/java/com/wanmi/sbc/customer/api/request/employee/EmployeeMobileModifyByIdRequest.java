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
 * com.wanmi.sbc.customer.api.request.employee.EmployeeMobileModifyRequest
 *
 * @author lipeng
 * @dateTime 2018/9/11 上午9:45
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeMobileModifyByIdRequest implements Serializable {


    private static final long serialVersionUID = -2641907019938923226L;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    @NotNull
    protected String employeeId;

    /**
     * 员工手机号
     */
    @ApiModelProperty(value = "员工手机号")
    private String mobile;
}
