package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -7196380103083045476L;

    @ApiModelProperty(value = "会员ID")
    private String customerId;

    @ApiModelProperty(value = "账户")
    private String customerAccount;
}
