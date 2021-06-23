package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeNameExistsResponse implements Serializable {

    private static final long serialVersionUID = -5214389668406966629L;

    @ApiModelProperty(value = "是否存在")
    private boolean exists;
}
