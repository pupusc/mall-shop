package com.wanmi.sbc.order.pointstrade.fsm.params;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.pointstrade.fsm.event.PointsTradeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态扭转 请求参数
 * Created by jinwei on 29/3/2017.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsTradeStateRequest {

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 操作人
     */
    private Operator operator;

    /**
     * 事件操作
     */
    private PointsTradeEvent event;

    /**
     *
     */
    private Object data;
}
