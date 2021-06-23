package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.TradeStateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huqingjie
 * @Date 2021.3.24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeGetBookingTypeByIdResponse implements Serializable {

    private static final long serialVersionUID = -8492593585089123854L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String id;

    /**
     * 预售类型
     */
    @ApiModelProperty(value = "预售类型")
    private BookingType bookingType;

    /**
     * 订单总体状态
     */
    @ApiModelProperty(value = "订单总体状态")
    private TradeStateVO tradeState;
}
