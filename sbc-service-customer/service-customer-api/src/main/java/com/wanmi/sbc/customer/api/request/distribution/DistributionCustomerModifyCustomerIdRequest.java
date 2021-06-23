package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新会员Id对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class DistributionCustomerModifyCustomerIdRequest implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotNull
    private String customerId;
    /**
     * 原来会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotNull
    private String oldCustomerId;


}
