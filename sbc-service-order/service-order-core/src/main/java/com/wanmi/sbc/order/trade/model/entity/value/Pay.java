package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付信息
 *
 * @author wumeng[of2627]
 *         company qianmi.com
 *         Date 2017-04-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pay implements Serializable {
    /**
     * 订单号
     */
    private String tid;
    /**
     * 支付顺序：1 先付 2 后付
     */
    private Integer payOrder;
    /**
     * 支付方编号
     */
    private String userId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;

}
