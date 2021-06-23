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
public class EsEmployeeHandoverRequest implements Serializable {

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
