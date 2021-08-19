package com.wanmi.sbc.order.trade.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.order.bean.enums.GrouponOrderCheckStatus;
import com.wanmi.sbc.order.bean.enums.PayState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午7:57 2019/5/15
 * @Description: 订单拼团信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeGroupon {

    /**
     * 团号
     */
    private String grouponNo;

    /**
     * 拼团活动编号
     */
    private String grouponActivityId;

    /**
     * 商品id
     */
    private String goodInfoId;

    /**
     * spu
     */
    private String goodId;

    /**
     * 退单商品数量
     */
    private Integer returnNum = 0;

    /**
     * 退单金额
     */
    private BigDecimal returnPrice = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 成团时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime grouponSuccessTime;

    /**
     * 团失败时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime failTime;


    /**
     * 拼团状态
     */
    private GrouponOrderStatus grouponOrderStatus;

    /**
     * 是否团长
     */
    private Boolean leader;

    /**
     * 团失败原因
     */
    private GrouponOrderCheckStatus grouponFailReason;

    /**
     * 支付状态
     */
    private PayState payState;

}
