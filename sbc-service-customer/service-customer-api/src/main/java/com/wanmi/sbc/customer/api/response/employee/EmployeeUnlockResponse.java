package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeUnlockResponse implements Serializable {

    private static final long serialVersionUID = -4371796233925509037L;

    @ApiModelProperty(value = "总数")
    private int count;
}
