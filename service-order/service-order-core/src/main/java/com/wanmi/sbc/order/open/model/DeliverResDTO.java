package com.wanmi.sbc.order.open.model;

import com.wanmi.sbc.open.vo.ConsigneeResDTO;
import com.wanmi.sbc.open.vo.DeliverItemResDTO;
import com.wanmi.sbc.open.vo.LogisticsResDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-02-17 15:07:00
 */
@Data
public class DeliverResDTO {
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
    private ConsigneeResDTO consignee;

    /**
     * 物流信息
     */
    private LogisticsResDTO logistics;

    /**
     * 发货清单
     */
    private List<DeliverItemResDTO> deliverItems = new ArrayList<>();
}
