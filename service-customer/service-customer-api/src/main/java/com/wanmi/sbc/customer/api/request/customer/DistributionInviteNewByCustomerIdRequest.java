package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 邀请记录分页查询参数
 * Created by baijz on 2019/3/08.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewByCustomerIdRequest{
    /**
     * 受邀人ID
     */
    @NotNull
    @ApiModelProperty(value = "受邀人ID")
    private String  invitedNewCustomerId;

}
