package com.wanmi.sbc.order.bean.dto.yzorder.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 周期购发货记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodOrderDeliverDTO implements Serializable {

    private static final long serialVersionUID = 947249287199933638L;

    /**
     * 店铺id
     */
    private Long kdt_id;

    /**
     * 主订单id
     */
    private String tid;

    /**
     * 子订单ID
     */
    private String oid;

    /**
     * 期数
     */
    private Integer issue;

    /**
     * 多期发货记录配送信息
     */
    private List<MultiPeriodDistInfoDTO> multi_period_dist_info;
}
