package com.wanmi.sbc.order.trade.request;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import lombok.*;

import java.util.List;

/**
 *  更新卡券库存请求参数
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualCouponUpdateRequest {

    /**
     *  处理商品
     */
    List<TradeItem> tradeItems;

    /**
     * true:减库存  false:加库存
     */
    boolean subFlag;
    /**
     *  订单编号
     */
    String tradeId;

    /**
     * 操作人
     */
    Operator operator;
}
