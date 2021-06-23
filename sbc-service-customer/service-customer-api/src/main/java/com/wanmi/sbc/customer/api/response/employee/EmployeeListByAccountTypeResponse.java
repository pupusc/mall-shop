package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeListByAccountTypeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EmployeeListByAccountTypeResponse implements Serializable {

    private static final long serialVersionUID = -3176228928843484973L;

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeeListByAccountTypeVO> employeeList;
}
