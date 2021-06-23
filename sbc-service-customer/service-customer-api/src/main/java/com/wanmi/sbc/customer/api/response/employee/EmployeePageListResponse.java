package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class EmployeePageListResponse {

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeePageVO> employeeList;
}
