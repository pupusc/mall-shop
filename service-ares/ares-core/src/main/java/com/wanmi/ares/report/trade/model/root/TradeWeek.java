package com.wanmi.ares.report.trade.model.root;

import lombok.Data;

import java.time.LocalDate;

/**
 * trade_week
 * @author 
 */
@Data
public class TradeWeek extends TradeBase {
    /**
     * 统计月份
     */
    private String month;

    /**
     * 统计类型，0：：最近30天按周统计，1：最近6个月按周统计
     */
    private Integer type;

    /**
     * 按周统计，周开始日期
     */
    private LocalDate weekStartDate;

    /**
     * 按周统计，周结束日期
     */
    private LocalDate weekEndDate;

}