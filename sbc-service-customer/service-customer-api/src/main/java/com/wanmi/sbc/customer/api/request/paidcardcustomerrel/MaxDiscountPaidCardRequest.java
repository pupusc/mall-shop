package com.wanmi.sbc.customer.api.request.paidcardcustomerrel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MaxDiscountPaidCardRequest implements Serializable {

    @ApiModelProperty("用户id")
    private String customerId;

}
