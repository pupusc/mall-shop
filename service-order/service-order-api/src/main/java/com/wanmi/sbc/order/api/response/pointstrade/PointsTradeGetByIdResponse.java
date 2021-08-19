package com.wanmi.sbc.order.api.response.pointstrade;

import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradeGetByIdResponse
 * @Description 根据积分订单id获取积分订单详情返回的Response
 * @Author lvzhenwei
 * @Date 2019/5/10 13:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeGetByIdResponse implements Serializable {
    private static final long serialVersionUID = 4727241801549384116L;

    /**
     * 积分订单详情信息
     */
    @ApiModelProperty(value = "积分订单详情信息")
    private PointsTradeVO pointsTradeVo;
}
