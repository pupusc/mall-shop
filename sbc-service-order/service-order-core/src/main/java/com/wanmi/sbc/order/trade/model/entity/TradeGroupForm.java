package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验团长是否可开团或者团员是否可参团
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeGroupForm implements Serializable, Cloneable {

    private static final long serialVersionUID = 2973899410241708605L;

    /**
     * 购买人-会员ID
     */
    private String buyCustomerId;

    /**
     * 拼团活动ID
     */
    private String grouponActivityId;

    /**
     * 团编号
     */
    private String grouponNo;

    /**
     * 是否团长
     */
    private Boolean leader;

    /**
     * SPU编号
     */
    private String goodsId;

    /**
     * 活动结束时间
     */
    private LocalDateTime grouponActivityEndTime;

    /**
     * 当前时间
     */
    private LocalDateTime currentTime;
}
