package com.wanmi.sbc.order.api.response.pointstrade;

import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PointsTradeListExportResponse
 * @Description 积分订单导出数据Response
 * @Author lvzhenwei
 * @Date 2019/5/10 15:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeListExportResponse implements Serializable {
    private static final long serialVersionUID = -690781213988229442L;

    /**
     * 积分订单列表
     */
    @ApiModelProperty(value = "积分订单列表")
    private List<PointsTradeVO> pointsTradeVOList;

}
