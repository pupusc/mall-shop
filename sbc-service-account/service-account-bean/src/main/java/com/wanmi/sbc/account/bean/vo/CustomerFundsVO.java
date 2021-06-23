package com.wanmi.sbc.account.bean.vo;

import com.wanmi.sbc.common.util.SensitiveUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金VO
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
public class CustomerFundsVO implements Serializable{

    /**
     * 主键
     */
    private String customerFundsId;

    /**
     * 会员登录账号|手机号
     */
    private String customerAccount;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 冻结余额
     */
    private BigDecimal blockedBalance;

    /**
     * 可提现金额
     */
    private BigDecimal withdrawAmount;

    /**
     * 已提现金额
     */
    private BigDecimal alreadyDrawAmount;

    /**
     * 是否分销员，0：否，1：是
     */
    private Integer distributor;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    public String getCustomerAccount() {
        return SensitiveUtils.handlerMobilePhone(customerAccount);
    }
}
