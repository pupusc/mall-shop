package com.wanmi.sbc.customer.api.request.paidcard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户付费卡查询实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPaidCardQueryRequest {

    @ApiModelProperty("会员ID")
    private String customerId;
}
