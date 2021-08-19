package com.wanmi.sbc.elastic.api.request.employee;

import com.wanmi.sbc.customer.bean.dto.EmployeeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsEmployeeImportRequest {

    /**
     * 员工列表
     */
    @ApiModelProperty(value = "员工列表")
    private List<EsEmployeeSaveRequest> employeeList;
}
