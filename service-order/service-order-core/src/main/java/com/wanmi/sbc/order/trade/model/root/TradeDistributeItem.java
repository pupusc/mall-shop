package com.wanmi.sbc.order.trade.model.root;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:40 2019/3/4
 * @Description: 下单分销单品信息
 */
@Data
public class TradeDistributeItem {

    /**
     * 单品id
     */
    private String goodsInfoId;

    /**
     * 购买数量
     */
    private Long num;

    /**
     * 总sku的实付金额
     */
    private BigDecimal actualPaidPrice;

    /**
     * 返利人佣金比例
     */
    private BigDecimal commissionRate;

    /**
     * 返利人佣金
     */
    private BigDecimal commission;

    /**
     * 总佣金(返利人佣金 + 提成人佣金)
     */
    private BigDecimal totalCommission = BigDecimal.ZERO;

    /**
     * 提成人佣金列表
     */
    private List<TradeDistributeItemCommission> commissions = new ArrayList<>();

}
