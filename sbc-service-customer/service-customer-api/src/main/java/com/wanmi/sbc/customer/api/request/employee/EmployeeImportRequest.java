package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.customer.bean.dto.EmployeeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class EmployeeImportRequest implements Serializable {
    private static final long serialVersionUID = -1801617784815839394L;

    /**
     * 员工列表
     */
    @ApiModelProperty(value = "员工列表")
    private List<EmployeeDTO> employeeList;
}
