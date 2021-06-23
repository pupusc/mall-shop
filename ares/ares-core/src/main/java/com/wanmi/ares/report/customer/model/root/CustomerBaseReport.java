package com.wanmi.ares.report.customer.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>客户维度统计视图基类</p>
 * Created by of628-wenzhi on 2017-09-27-下午6:02.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerBaseReport implements Serializable {

    @Id
    private String id;

    /**
     * 商户号
     */
    private String companyId;

    /**
     * 订单总笔数
     */
    private Long orderCount;

    /**
     * 订单总金额
     */
    private BigDecimal amount;

    /**
     * 订单商品总件数
     */
    private Long skuCount;

    /**
     * 付款总金额
     */
    private BigDecimal payAmount;

    /**
     * 付款订单数
     */
    private Long payOrderCount;

    /**
     * 笔单价
     */
    private BigDecimal orderPerPrice;

    /**
     * 退单总笔数
     */
    private Long returnCount;

    /**
     * 退单总金额
     */
    private BigDecimal returnAmount;

    /**
     * 退货商品总件数
     */
    private Long returnSkuCount;

    /**
     * 客户数量字段冗余，用于客单价计算
     */
    private Long customerCount;

    /**
     * 操作时间 用于时间聚合
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private static final long serialVersionUID = 6935436310851907693L;

    /**
     * 日期用于分区（日期分区）
     */
    private String baseDate;
}
