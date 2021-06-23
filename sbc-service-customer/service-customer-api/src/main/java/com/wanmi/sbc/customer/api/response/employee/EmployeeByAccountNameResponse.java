package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeByAccountNameResponse implements Serializable {

    private static final long serialVersionUID = -7809034522048994351L;


    @ApiModelProperty(value = "员工")
    private EmployeeVO employee;
}
