package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.PointsTradeCommitResultVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: yinxianzhi
 * @createDate: 2019/05/20 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PointsTradeCommitResponse implements Serializable {

    private static final long serialVersionUID = -8896514637363846221L;

    /**
     * 积分订单提交结果
     */
    @ApiModelProperty(value = "积分订单提交结果")
    private PointsTradeCommitResultVO pointsTradeCommitResult;

}
