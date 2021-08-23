package com.wanmi.ares.report.trade.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 交易报表
 * Created by sunkun on 2017/9/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeReport {

    /**
     * 商家id
     */
    @Id
    private String id = Constants.companyId;

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
    private BigDecimal orderAmt = new BigDecimal(0);

    /**
     * 付款订单数
     */
    private Long PayOrderCount = 0l;

    /**
     * 付款人数
     */
    private Long PayOrderNum = 0l;

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
     * 退单金额
     */
    private BigDecimal returnOrderAmt = new BigDecimal(0);

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;

}
