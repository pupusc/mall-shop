package com.soybean.mall.order.trade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportDetailDTO implements Serializable {
    private static final long serialVersionUID = -6888941670900308110L;

    private String orderId;
    private String goodsName;
    private String createTime;
    private BigDecimal price;
}
