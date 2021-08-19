package com.wanmi.ares.report.trade.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * trade_seven
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeBase implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 下单笔数
     */
    private Long orderNum;

    /**
     * 下单人数
     */
    private Long orderUserNum;

    /**
     * 下单金额
     */
    private BigDecimal orderMoney;

    /**
     * 付款订单数
     */
    private Long payNum;

    /**
     * 付款人数
     */
    private Long payUserNum;

    /**
     * 付款金额
     */
    private BigDecimal payMoney;

    /**
     * 下单转化率
     */
    private BigDecimal orderConversion;

    /**
     * 付款转化率
     */
    private BigDecimal payConversion;

    /**
     * 全店转化率
     */
    private BigDecimal allConversion;

    /**
     * 客单价
     */
    private BigDecimal userPerPrice;

    /**
     * 笔单价
     */
    private BigDecimal orderPerPrice;

    /**
     * 退单笔数
     */
    private Long refundNum;

    /**
     * 退单人数
     */
    private Long refundUserNum;

    /**
     * 退单金额
     */
    private BigDecimal refundMoney;

    /**
     * 访问人数
     */
    private Long uv;

    /**
     * 店铺标识
     */
    private Long companyId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 店铺名称
     */
    private String storeName;

    private static final long serialVersionUID = 1L;

}