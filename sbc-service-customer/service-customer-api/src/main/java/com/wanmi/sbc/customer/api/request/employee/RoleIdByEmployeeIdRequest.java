package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleIdByEmployeeIdRequest implements Serializable {

    private static final long serialVersionUID = -94434060715745422L;

    /**
     * 员工Id
     */
    @ApiModelProperty(value = "员工Id")
    @NotBlank
    private String employeeId;

}
