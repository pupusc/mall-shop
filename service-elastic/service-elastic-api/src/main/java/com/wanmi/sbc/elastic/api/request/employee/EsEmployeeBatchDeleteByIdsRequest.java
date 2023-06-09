package com.wanmi.sbc.elastic.api.request.employee;

import com.wanmi.sbc.common.enums.AccountType;
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
public class EsEmployeeBatchDeleteByIdsRequest implements Serializable {


    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private List<String> employeeIds;

    /**
     * 账户类型
     */
    @ApiModelProperty(value = "账户类型")
    private AccountType accountType;
}
