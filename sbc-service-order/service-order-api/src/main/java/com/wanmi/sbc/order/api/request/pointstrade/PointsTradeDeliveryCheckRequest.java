package com.wanmi.sbc.order.api.request.pointstrade;

import com.wanmi.sbc.order.bean.dto.TradeDeliverRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradeDeliveryCheckRequest
 * @Description 发货校验Request
 * @Author lvzhenwei
 * @Date 2019/5/21 16:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeDeliveryCheckRequest implements Serializable {

    private static final long serialVersionUID = 638914152382237786L;

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
