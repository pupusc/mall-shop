package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("删除付费会员请求实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDeletePaidCardRequest {

    @ApiModelProperty("会员id")
    private String customerId;

    @ApiModelProperty("付费会员卡关联关系id")
    private String paidCardRelId;
}
