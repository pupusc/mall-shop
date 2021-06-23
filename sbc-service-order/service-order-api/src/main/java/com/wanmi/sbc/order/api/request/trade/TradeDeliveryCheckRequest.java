package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeDeliverRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 19:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeDeliveryCheckRequest implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String tid;

    /**
     * 交易物流
     */
    @ApiModelProperty(value = "交易物流")
    private TradeDeliverRequestDTO tradeDeliver;

}
