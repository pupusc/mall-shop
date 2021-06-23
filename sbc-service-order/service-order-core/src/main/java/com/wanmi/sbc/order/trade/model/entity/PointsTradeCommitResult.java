package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>积分订单成功提交的返回信息</p>
 * Created by yinxianzhi on 2019-05-20-下午3:52.
 */
@Data
@AllArgsConstructor
public class PointsTradeCommitResult {

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 交易金额
     */
    private Long points;

}
