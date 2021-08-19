package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 邀新人数统计请求
 * @Autho qiaokang
 * @Date：2019-03-07 19:08:05
 */
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DistributionCountCouponByRequestCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = -8923620941540190722L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotNull
    private String customerId;

}
