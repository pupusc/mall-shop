package com.wanmi.sbc.elastic.bean.vo.customerFunds;

import com.wanmi.sbc.common.util.SensitiveUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金
 * Created by yangzhen on 2020/12/15.
 */
@ApiModel
@Data
public class EsCustomerFundsVO implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;
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
