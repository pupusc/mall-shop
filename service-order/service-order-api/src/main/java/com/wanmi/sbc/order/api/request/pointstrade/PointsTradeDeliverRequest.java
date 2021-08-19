package com.wanmi.sbc.order.api.request.pointstrade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.TradeDeliverDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradeDeliverRequest
 * @Description 积分订单确认收货Request
 * @Author lvzhenwei
 * @Date 2019/5/21 15:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeDeliverRequest implements Serializable {

    private static final long serialVersionUID = -3055412887779206888L;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 交易单物流信息
     */
    @ApiModelProperty(value = "交易单物流信息")
    private TradeDeliverDTO tradeDeliver;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
