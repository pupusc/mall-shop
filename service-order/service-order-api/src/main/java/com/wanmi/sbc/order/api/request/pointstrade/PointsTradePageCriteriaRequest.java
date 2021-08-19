package com.wanmi.sbc.order.api.request.pointstrade;

import com.wanmi.sbc.order.bean.dto.PointsTradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradePageCriteriaRequest
 * @Description 积分订单分页查询--搜索条件查询
 * @Author lvzhenwei
 * @Date 2019/5/10 14:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PointsTradePageCriteriaRequest implements Serializable {
    private static final long serialVersionUID = -8310228241310387521L;

    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数")
    private PointsTradeQueryDTO pointsTradePageDTO;
}
