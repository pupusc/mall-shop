package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:04 2019/5/28
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeGrouponCommitForm {

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    private Boolean openGroupon;

    /**
     * 团号
     */
    private String grouponNo;

    /**
     * 拼团活动ID
     */
    private String grouponActivityId;

    /**
     * 拼团活动商品限购数
     */
    private Integer limitSellingNum;

    /**
     * 拼团价格
     */
    private BigDecimal grouponPrice;

    /**
     * 拼团活动是否包邮
     */
    private boolean freeDelivery;

}
