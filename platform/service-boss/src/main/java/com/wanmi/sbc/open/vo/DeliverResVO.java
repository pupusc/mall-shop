package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-02-17 15:07:00
 */
@Data
public class DeliverResVO {
    /**
     * 发货单号
     */
    private String deliverId;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 收货人信息
     */
    private ConsigneeResVO consignee;

    /**
     * 物流信息
     */
    private LogisticsResVO logistics;

    /**
     * 发货清单
     */
    private List<DeliverItemResVO> deliverItems = new ArrayList<>();
}
