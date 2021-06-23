package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员资金-更新会员账号对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsModifyCustomerAccountByCustomerIdRequest implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 会员账号
     */
    @ApiModelProperty(value = "会员账号")
    private String customerAccount;
}
