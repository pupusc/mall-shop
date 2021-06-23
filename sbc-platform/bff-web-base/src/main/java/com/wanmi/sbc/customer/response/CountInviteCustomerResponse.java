package com.wanmi.sbc.customer.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CountInviteCustomerResponse {

    @ApiModelProperty(value = "邀新人数")
    private Long inviteNum;

    @ApiModelProperty(value = "有效邀新人数")
    private Long validInviteNum;

    @ApiModelProperty(value = "我的客户")
    private Integer myCustomerNum;
}
