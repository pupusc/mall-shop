package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountModifyResponse implements Serializable {
    private static final long serialVersionUID = 2887473671448606656L;

    @ApiModelProperty(value = "总数")
    private int count;
}
