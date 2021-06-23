package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAdjustDepartmentRequest implements Serializable {
    private static final long serialVersionUID = 7537215279788528855L;

    /**
     * 员工列表
     */
    private List<String> employeeIds;

    /**
     * 部门列表
     */
    private List<Long> departmentIds;
}
