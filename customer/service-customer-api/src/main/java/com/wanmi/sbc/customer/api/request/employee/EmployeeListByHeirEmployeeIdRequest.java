package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListByHeirEmployeeIdRequest implements Serializable {

    private static final long serialVersionUID = 900487494332156984L;


    @ApiModelProperty(value = "交接人ID")
    private List<String> heirEmployeeId;

}
