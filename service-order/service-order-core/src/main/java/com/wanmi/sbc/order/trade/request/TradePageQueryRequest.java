package com.wanmi.sbc.order.trade.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>客户端订单分页查询参数结构</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:34.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradePageQueryRequest extends BaseQueryRequest {

    /**
     * 订单流程状态
     */
    private FlowState flowState;

    /**
     * 订单支付状态
     */
    private PayState payState;

    /**
     * 订单发货状态
     */
    private DeliverStatus deliverStatus;

    /**
     * 下单时间上限，精度到天
     */
    private String createdFrom;

    /**
     * 下单时间下限,精度到天
     */
    private String createdTo;

    /**
     * 关键字，用于搜索订单编号或商品名称
     */
    private String keywords;

    /**
     * 邀请人id
     */
    private String inviteeId;

    /**
     * 分销渠道类型
     */
    private ChannelType channelType;
}
