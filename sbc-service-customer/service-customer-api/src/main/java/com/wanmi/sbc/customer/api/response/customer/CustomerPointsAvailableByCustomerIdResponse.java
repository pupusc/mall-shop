package com.wanmi.sbc.customer.api.response.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsAvailableByCustomerIdResponse implements Serializable {
    private static final long serialVersionUID = 2124512794369414457L;

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;
}
