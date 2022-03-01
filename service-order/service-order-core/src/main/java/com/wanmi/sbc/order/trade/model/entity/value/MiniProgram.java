package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小程序相关信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MiniProgram {
    /**
     * 订单状态同步结果1同步完成0未同步完成
     */
    private Integer syncStatus;

    /**
     * 同步过的物流信息
     */
    private List<TradeDeliver> delivery;
}
