package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.EmployeeListByIdsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
public class EmployeeListByIdsResponse implements Serializable {

    private static final long serialVersionUID = 8822442596957066011L;

    @ApiModelProperty(value = "业务员列表")
    private List<EmployeeListByIdsVO> employeeList;
}
