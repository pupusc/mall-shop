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
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGetByIdRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -1709264705378683734L;

    @ApiModelProperty(value = "客户ID")
    @NotNull
    private String customerId;
}
