package com.wanmi.sbc.order.api.request.pointstrade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradeGetByIdRequest
 * @Description 根据积分订单id获取积分订单详情request
 * @Author lvzhenwei
 * @Date 2019/5/10 14:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeGetByIdRequest implements Serializable {

    private static final long serialVersionUID = 4642377039051829555L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String tid;

    /**
     * 是否需要查询linkedmall子订单
     */
    @ApiModelProperty(value = "是否需要查询linkedmall子订单")
    private Boolean needLmOrder;
}
