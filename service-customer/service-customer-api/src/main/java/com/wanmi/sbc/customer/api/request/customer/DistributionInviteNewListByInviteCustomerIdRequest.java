package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 邀新记录列表查询请求体
 * @Autho qiaokang
 * @Date：2019-03-07 20:48:25
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionInviteNewListByInviteCustomerIdRequest implements Serializable {

    /**
     * 受邀人ID
     */
    @ApiModelProperty(value = "受邀人ID")
    private String invitedCustomerId;
}
