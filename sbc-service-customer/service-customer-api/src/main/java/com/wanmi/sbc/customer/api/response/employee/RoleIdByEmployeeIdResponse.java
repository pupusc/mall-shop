package com.wanmi.sbc.customer.api.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleIdByEmployeeIdResponse implements Serializable {

    private static final long serialVersionUID = -2289087137511933309L;

    //角色ID
    @ApiModelProperty(value = "角色ID")
    private String roleId;
}
