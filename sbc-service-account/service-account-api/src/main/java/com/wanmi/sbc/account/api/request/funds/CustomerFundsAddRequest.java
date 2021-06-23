package com.wanmi.sbc.account.api.request.funds;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-新增对象
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsAddRequest implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 会员账号
     */
    @ApiModelProperty(value = "会员账号")
    private String customerAccount;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;


    /**
     * 账户余额
     */
    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance;

    /**
     * 是否是分销员
     */
    @ApiModelProperty(value = "是否是分销员，0：否 1：是")
    private Integer distributor = NumberUtils.INTEGER_ZERO;
}
