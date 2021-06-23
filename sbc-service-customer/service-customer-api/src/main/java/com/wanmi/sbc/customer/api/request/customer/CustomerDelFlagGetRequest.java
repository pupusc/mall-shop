package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDelFlagGetRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -6700338370684244828L;

    @ApiModelProperty(value = "会员ID")
    @NotNull
    private String customerId;
}
