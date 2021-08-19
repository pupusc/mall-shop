package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Api
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeByNameResponse {

    /**
     * 业务员列表
     */
    @ApiModelProperty(value = "业务员列表")
    private List<Map<String, String>> employeeNames;
}
