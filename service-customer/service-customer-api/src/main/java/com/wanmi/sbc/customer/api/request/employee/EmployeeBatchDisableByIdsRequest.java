package com.wanmi.sbc.customer.api.request.employee;


import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.customer.bean.enums.AccountState;
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
public class EmployeeBatchDisableByIdsRequest implements Serializable {

    private static final long serialVersionUID = -3361294127200710100L;

    /**
     * 员工Id
     */
    @ApiModelProperty(value = "员工Id")
    private List<String> employeeIds;

    /**
     * 账号状态 0:启用 1:禁用
     */
    @ApiModelProperty(value = "账号状态")
    private AccountState accountState;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    @CanEmpty
    private String accountDisableReason;
}
