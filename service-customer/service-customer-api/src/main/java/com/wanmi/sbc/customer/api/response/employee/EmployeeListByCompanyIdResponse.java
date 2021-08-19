package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeListByCompanyIdVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EmployeeListByCompanyIdResponse implements Serializable {

    private static final long serialVersionUID = 526250159567290800L;

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeeListByCompanyIdVO> employeeList;
}
