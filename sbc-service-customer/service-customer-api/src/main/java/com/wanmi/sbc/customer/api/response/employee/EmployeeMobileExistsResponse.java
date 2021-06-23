package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class EmployeeMobileExistsResponse implements Serializable {

    private static final long serialVersionUID = -152656175135098604L;

    @ApiModelProperty(value = "是否存在")
    private boolean exists;
}
