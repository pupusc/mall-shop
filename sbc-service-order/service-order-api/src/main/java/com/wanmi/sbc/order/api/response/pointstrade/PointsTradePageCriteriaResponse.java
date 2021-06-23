package com.wanmi.sbc.order.api.response.pointstrade;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsTradePageCriteriaResponse
 * @Description 分页积分订单信息
 * @Author lvzhenwei
 * @Date 2019/5/10 14:18
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PointsTradePageCriteriaResponse implements Serializable {
    private static final long serialVersionUID = -5786005540353628619L;

    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据")
    private MicroServicePage<PointsTradeVO> pointsTradePage;
}
