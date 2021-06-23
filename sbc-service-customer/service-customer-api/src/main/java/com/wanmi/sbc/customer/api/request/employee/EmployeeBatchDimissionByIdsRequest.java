package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Api
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeBatchDimissionByIdsRequest implements Serializable {
    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private List<String> employeeIds;

    /**
     * 账户类型
     */
    @ApiModelProperty(value = "账户类型")
    private AccountState accountState;

    /**
     * 离职原因
     */
    @ApiModelProperty(value = "离职原因")
    @CanEmpty
    private String accountDimissionReason;
}
