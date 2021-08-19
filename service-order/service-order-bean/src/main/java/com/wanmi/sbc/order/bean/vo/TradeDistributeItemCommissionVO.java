package com.wanmi.sbc.order.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:47 2019/6/21
 * @Description: 分销单品佣金信息
 */
@Data
public class TradeDistributeItemCommissionVO implements Serializable{

    /**
     * 提成人会员id
     */
    private String customerId;

    /**
     * 提成人分销员id
     */
    private String distributorId;

    /**
     * 总佣金
     */
    private BigDecimal commission;

}
