package com.wanmi.sbc.order.trade.model.root;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:19 2019/6/19
 * @Description: 分销佣金提成信息
 */
@Data
public class TradeCommission {

    /**
     * 提成人会员id
     */
    private String customerId;

    /**
     * 提成人名称
     */
    private String customerName;

    /**
     * 提成人分销员id
     */
    private String distributorId;

    /**
     * 佣金提成
     */
    private BigDecimal commission;

}
