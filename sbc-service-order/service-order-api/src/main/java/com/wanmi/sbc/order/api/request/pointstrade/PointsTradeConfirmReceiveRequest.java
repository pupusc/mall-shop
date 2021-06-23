package com.wanmi.sbc.order.api.request.pointstrade;

import com.wanmi.sbc.common.base.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PointsTradeConfirmReceiveRequest
 * @Description 积分订单确认收货参数
 * @Author lvzhenwei
 * @Date 2019/5/21 15:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeConfirmReceiveRequest {

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
