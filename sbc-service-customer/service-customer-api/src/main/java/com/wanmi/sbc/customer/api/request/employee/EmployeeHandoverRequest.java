package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeHandoverRequest implements Serializable {
    private static final long serialVersionUID = 5951511116799057374L;

    /**
     * 原业务员集合
     */
    @ApiModelProperty(value = "原业务员集合")
    private List<String> employeeIds;

    /**
     * 新业务原id
     */
    @ApiModelProperty("新业务原id")
    private String newEmployeeId;
}
