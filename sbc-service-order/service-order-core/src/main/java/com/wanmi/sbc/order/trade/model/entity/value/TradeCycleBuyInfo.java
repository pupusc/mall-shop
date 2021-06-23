package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.order.bean.dto.CycleBuySendDateRuleDTO;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 周期购信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeCycleBuyInfo {

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    private CycleBuySendDateRuleDTO cycleBuySendDateRule;

    /**
     * 期数
     */
    private Integer cycleNum;

    /**
     * 周期购发货日历
     */
    private List<DeliverCalendar> deliverCalendar;

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    private DeliveryPlan deliveryPlan;
}
