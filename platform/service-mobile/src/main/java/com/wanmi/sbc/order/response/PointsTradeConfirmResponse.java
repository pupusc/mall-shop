package com.wanmi.sbc.order.response;

import com.wanmi.sbc.order.bean.vo.PointsTradeItemGroupVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: yinxianzhi
 * @Date: Created In 下午5:58 2019/5/20
 * @Description: 积分订单确认返回结构
 */
@ApiModel
@Data
public class PointsTradeConfirmResponse {

    /**
     * 积分订单项
     */
    @ApiModelProperty(value = "积分订单项")
    private PointsTradeItemGroupVO pointsTradeConfirmItem;

    /**
     * 订单积分数
     */
    @ApiModelProperty(value = "订单积分数")
    private Long totalPoints;
}