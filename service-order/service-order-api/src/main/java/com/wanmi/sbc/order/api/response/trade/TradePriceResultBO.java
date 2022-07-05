package com.wanmi.sbc.order.api.response.trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-28 13:40:00
 */
@Data
public class TradePriceResultBO {
    private BigDecimal totalPrice;
    private BigDecimal cutPrice;
    private BigDecimal payPrice;
    private List<PriceItem> totalPriceItems = new ArrayList<>();
    private List<PriceItem> cutPriceItems = new ArrayList<>();

    @Data
    public static class PriceItem {
        /**
         * 增加类型：101:商品费用；102:配送费用
         * 减少类型：201:会员折扣；202:优惠券抵扣；203:营销促销；
         */
        private Integer type;
        /**
         * vip折扣；优惠券抵扣；营销活动优惠；
         */
        private String desc;
        /**
         * 金额
         */
        private BigDecimal amount;
    }

    @Getter
    @AllArgsConstructor
    public enum PriceItemType {
        ADD_GOODS(101, "商品费用"),
        ADD_DELIVERY(102, "配送费用"),
        SUB_VIP_RATE(201, "会员折扣"),
        SUB_COUPON_COST(202, "优惠券抵扣"),
        SUB_PROMOTE_MKT(203, "营销促销");

        private Integer code;
        private String desc;
    }
}
