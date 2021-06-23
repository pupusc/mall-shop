package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员资金-根据会员ID查询对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsByCustomerIdRequest implements Serializable {

    /**
     * 会员Id
     */
    @ApiModelProperty(value = "会员Id")
    private String customerId;
}
