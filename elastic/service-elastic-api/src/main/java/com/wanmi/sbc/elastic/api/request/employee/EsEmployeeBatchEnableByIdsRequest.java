package com.wanmi.sbc.elastic.api.request.employee;


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
public class EsEmployeeBatchEnableByIdsRequest implements Serializable {

    @ApiModelProperty(value = "员工编号")
    List<String> employeeIds;
}
