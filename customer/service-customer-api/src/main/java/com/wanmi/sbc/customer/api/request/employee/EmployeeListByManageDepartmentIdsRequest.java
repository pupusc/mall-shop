package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListByManageDepartmentIdsRequest implements Serializable {

    private static final long serialVersionUID = 900487494332156984L;

    /**
     * 管理部门
     */
    @ApiModelProperty(value = "管理部门")
    @NotBlank
    private String manageDepartmentIds;
}
