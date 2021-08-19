package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeDisableOrEnableByCompanyIdVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EmployeeDisableOrEnableByCompanyIdResponse implements Serializable {

    private static final long serialVersionUID = -3458208828396438895L;

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeeDisableOrEnableByCompanyIdVO> employeeList;
}
