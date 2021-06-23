package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;


/**
 * 周期购订单查询发货日历入参
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ShippingCalendarRequest implements Serializable {

    private static final long serialVersionUID = 3976362856981544921L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NonNull
    private String tid;


}
