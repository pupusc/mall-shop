package com.wanmi.ares.report.trade.model.reponse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 交易报表查询，返回格式
 * Created by sunkun on 2017/10/12.
 */
@Data
public class TradeReponse {

    /**
     * 商家id
     */
    private String id;

    /**
     * 下单笔数
     */
    private Long orderCount = 0L;

    /**
     * 下单人数
     */
    private Long orderNum = 0L;

    /**
     * 下单金额
     */
    private BigDecimal orderAmt = BigDecimal.ZERO;

    /**
     * 付款订单数
     */
    private Long PayOrderCount = 0L;

    /**
     * 付款人数
     */
    private Long PayOrderNum = 0L;

    /**
     * 付款金额
     */
    private BigDecimal payOrderAmt = BigDecimal.ZERO;

    /**
     * 下单转化率
     */
    private BigDecimal orderConversionRate = BigDecimal.ZERO;

    /**
     * 付款转化率
     */
    private BigDecimal payOrderConversionRate = BigDecimal.ZERO;

    /**
     * 全店转化率
     */
    private BigDecimal wholeStoreConversionRate = BigDecimal.ZERO;

    /**
     * 客单价
     */
    private BigDecimal customerUnitPrice = BigDecimal.ZERO;

    /**
     * 笔单价
     */
    private BigDecimal everyUnitPrice = BigDecimal.ZERO;

    /**
     * 退单笔数
     */
    private Long returnOrderCount = 0L;

    /**
     * 退单人数
     */
    private Long returnOrderNum = 0L;

    /**
     * 全店uv
     */
    private Long totalUv = 0L;

    /**
     * 退单金额
     */
    private BigDecimal returnOrderAmt = BigDecimal.ZERO;

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate time;

    private String title;
}
