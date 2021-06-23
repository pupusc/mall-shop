package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeCycleBuyInfoDTO implements Serializable {

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    @ApiModelProperty(value = "用户选择的发货日期")
    private CycleBuySendDateRuleDTO cycleBuySendDateRule;

    /**
     * 期数
     */
    @ApiModelProperty(value = "期数")
    private Integer cycleNum;

    /**
     * 周期购发货日历
     */
    @ApiModelProperty(value = "周期购发货日历")
    private List<DeliverCalendarDTO> deliverCalendar;

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    @ApiModelProperty(value = "配送方案")
    private DeliveryPlan deliveryPlan;
}
