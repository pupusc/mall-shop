package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class CustomerSimplifyByIdRequest implements Serializable {
    private static final long serialVersionUID = 2124512794369414457L;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;
}
