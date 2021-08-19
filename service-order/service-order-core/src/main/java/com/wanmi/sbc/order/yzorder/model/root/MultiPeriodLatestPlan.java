package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 周期购订单最新一期的发货记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodLatestPlan {

    /**
     * 订单号
     */
    private String tid;

    /**
     * 子订单id
     */
    private String oid;

    /**
     * 最新一期多期发货计划
     */
    private MultiPeriodPlan multiPeriodPlan;

    /**
     * 多期发货明细
     */
    private MultiPeriodDetail multiPeriodDetail;


}
