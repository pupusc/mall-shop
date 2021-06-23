package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员资金-更新会员名称对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsModifyCustomerNameByCustomerIdRequest implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;


    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;
}
