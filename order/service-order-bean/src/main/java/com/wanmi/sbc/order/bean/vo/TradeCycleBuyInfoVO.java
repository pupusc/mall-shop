package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeCycleBuyInfoVO implements Serializable {

    private static final long serialVersionUID = -4561303453849847015L;

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    @ApiModelProperty(value = "用户选择的发货日期")
    private CycleBuySendDateRuleVO cycleBuySendDateRule;

    /**
     * 期数
     */
    @ApiModelProperty(value = "期数")
    private Integer cycleNum;

    /**
     * 周期购发货日历
     */
    @ApiModelProperty(value = "周期购发货日历")
    private List<DeliverCalendarVO> deliverCalendar;

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    @ApiModelProperty(value = "配送方案")
    private DeliveryPlan deliveryPlan;

    /**
     * 下次发货周几
     */
    @ApiModelProperty(value = "配送方案")
    private Integer week;

    /**
     * 下次发货日期
     */
    @ApiModelProperty(value = "下次发货日期")
    private String localTime;

    /**
     * 下一期期数
     */
    @ApiModelProperty(value = "下一期期数")
    private Integer numberPeriods;

    /**
     * 已发期数
     */
    @ApiModelProperty(value = "已发期数")
    private Integer alreadyCycleNum;
}
