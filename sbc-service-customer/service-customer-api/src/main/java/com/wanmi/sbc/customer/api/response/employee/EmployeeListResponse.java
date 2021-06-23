package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EmployeeListResponse implements Serializable {

    private static final long serialVersionUID = -2289087137511933309L;

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeeListVO> employeeList;
}
