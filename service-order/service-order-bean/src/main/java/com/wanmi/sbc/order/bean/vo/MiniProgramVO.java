package com.wanmi.sbc.order.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MiniProgramVO implements Serializable {
    private static final long serialVersionUID = -1616490114063696342L;

    /**
     * 订单状态同步结果1同步完成0未同步完成
     */
    private Integer syncStatus = 0;

    /**
     * 同步过的物流信息
     */
    private List<TradeDeliverVO> delivery;
}
