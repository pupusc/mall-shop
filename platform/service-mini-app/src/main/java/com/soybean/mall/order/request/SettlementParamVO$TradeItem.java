package com.soybean.mall.order.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettlementParamVO$TradeItem implements Serializable {
    /**
     * skuId
     */
    private String skuId;
    /**
     * 购买数量
     */
    private long count;
//    /**
//     * 购买商品的价格
//     */
//    private BigDecimal price;
//    /**
//     * 是否是秒杀抢购商品
//     */
//    //private Boolean isFlashSaleGoods;
//
//    /**
//     * 秒杀抢购商品Id
//     */
//    //private Long flashSaleGoodsId;
//    /**
//     * 是否是预约抢购商品
//     */
//    //private Boolean isAppointmentSaleGoods = Boolean.FALSE;
//
//    /**
//     * 抢购活动Id
//     */
//    //private Long appointmentSaleId;
//
//    /**
//     * 是否是预售商品
//     */
//    //private Boolean isBookingSaleGoods = Boolean.FALSE;
//
//    /**
//     * 预售活动Id
//     */
//    //private Long bookingSaleId;
//
//    /**
//     * 配送周期 0:每日一期 1:每周一期 2:每月一期
//     */
//    //private DeliveryCycle deliveryCycle;
//
//    /**
//     * 用户选择的发货日期
//     */
//    //private String sendDateRule;
//
//    /**
//     *  配送方案 0:商家主导配送 1:客户主导配送
//     */
//    //private DeliveryPlan deliveryPlan;
}
