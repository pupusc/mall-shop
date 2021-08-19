package com.wanmi.sbc.order.bean.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 周期购订单发货日历VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Builder
public class ShippingCalendarVO {


    /**
     * 周期订单发货日期
     */
    @ApiModelProperty(value = "周期订单发货日期")
    private List<DeliverCalendarVO> deliverCalendarVOS;


    /**
     * 已送期数
     */
    @ApiModelProperty(value = " 已送期数")
    private Integer numberPeriods;

    /**
     * 总期数
     */
    @ApiModelProperty(value = " 总期数")
    private Integer totalIssues;




}
