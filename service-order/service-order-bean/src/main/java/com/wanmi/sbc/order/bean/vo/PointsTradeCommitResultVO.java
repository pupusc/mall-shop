package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>积分订单成功提交的返回信息</p>
 * Created by yinxianzhi on 2019-05-20-下午3:52.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PointsTradeCommitResultVO {

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tid;

    /**
     * 交易积分数
     */
    @ApiModelProperty(value = "交易积分数")
    private Long points;

}
