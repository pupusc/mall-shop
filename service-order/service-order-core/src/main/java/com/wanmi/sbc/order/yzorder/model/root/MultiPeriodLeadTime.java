package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配送提前期
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodLeadTime {

    /**
     * 截止几点之前下单
     */
    private Integer lead_hour;

    /**
     * 提前天数
     */
    private Integer lead_day;
}
