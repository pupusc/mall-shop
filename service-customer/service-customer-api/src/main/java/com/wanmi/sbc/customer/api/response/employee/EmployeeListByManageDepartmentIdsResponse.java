package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListByManageDepartmentIdsResponse implements Serializable {

    private static final long serialVersionUID = -2289087137511933309L;

    @ApiModelProperty(value = "业务员ID列表")
    private List<String> employeeIdList;
}
