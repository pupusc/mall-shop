package com.wanmi.sbc.customer.api.response.employee;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeLoginErrorCountModifyByIdResponse implements Serializable {

    private static final long serialVersionUID = 5689141819823872847L;

    @ApiModelProperty(value = "总数")
    private int count;
}
