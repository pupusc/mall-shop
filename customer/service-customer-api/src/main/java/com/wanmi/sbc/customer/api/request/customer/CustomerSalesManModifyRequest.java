package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomerSalesManModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 2683001656142888586L;

    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    @ApiModelProperty(value = "账号类型")
    private AccountType accountType;
}
