package com.wanmi.sbc.customer.api.response.distribution;

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
public class DistributionCustomerForbiddenFlagBatchModifyResponse implements Serializable {
    private static final long serialVersionUID = -7264067544320945188L;

    @ApiModelProperty(value = "总数")
    private int count;
}
