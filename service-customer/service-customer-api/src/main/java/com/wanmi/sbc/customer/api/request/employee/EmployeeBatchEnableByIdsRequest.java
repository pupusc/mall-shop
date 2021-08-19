package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.customer.api.request.employee.EmployeeBatchEnableReqeust
 *
 * @author lipeng
 * @dateTime 2018/9/11 上午10:05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeBatchEnableByIdsRequest implements Serializable {

    private static final long serialVersionUID = 7727257406079298589L;

    @ApiModelProperty(value = "员工编号")
    List<String> employeeIds;
}
