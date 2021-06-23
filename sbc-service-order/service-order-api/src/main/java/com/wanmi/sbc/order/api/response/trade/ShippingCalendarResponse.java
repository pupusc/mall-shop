package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.ShippingCalendarVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ShippingCalendarResponse implements Serializable {

    /**
     * 周期订单发货日历
     */
    @ApiModelProperty(value = " 周期订单发货日历")
    private ShippingCalendarVO shippingCalendarVO;


}
