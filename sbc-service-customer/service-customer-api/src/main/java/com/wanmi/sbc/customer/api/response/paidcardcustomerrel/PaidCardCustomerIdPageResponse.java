package com.wanmi.sbc.customer.api.response.paidcardcustomerrel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardCustomerIdPageResponse implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private List<String> customerIds;
}
