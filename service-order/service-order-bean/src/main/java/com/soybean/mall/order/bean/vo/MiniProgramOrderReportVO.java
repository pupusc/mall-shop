package com.soybean.mall.order.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class MiniProgramOrderReportVO implements Serializable {
    private static final long serialVersionUID = 7462540572836605641L;
    /**
     * 总金额
     */
    private BigDecimal totalPrice = new BigDecimal(0);

    /**
     * 分时金额
     */
    private Map<String,BigDecimal> hourPrice;

    private List<OrderReportDetailVO> orders;

    @Data
    public static class OrderReportDetailVO{
        private String orderId;
        private String pic;
        private String goodsName;
        private String createTime;
        private BigDecimal price;
    }
}
