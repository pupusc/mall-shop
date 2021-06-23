package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-根据会员资金ID查询
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class CustomerFundsByCustomerFundsIdResponse extends CustomerFundsVO implements Serializable {

    /**
     * 收入笔数
     */
    private Long income;

    /**
     * 收入金额
     */
    private BigDecimal amountReceived;

    /**
     * 支出笔数
     */
    private Long expenditure;

    /**
     * 支出金额
     */
    private BigDecimal amountPaid;
}
