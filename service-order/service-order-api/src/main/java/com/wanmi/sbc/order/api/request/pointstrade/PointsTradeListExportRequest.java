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
 * @ClassName PointsTradeListExportRequest
 * @Description 积分订单导出Request参数
 * @Author lvzhenwei
 * @Date 2019/5/10 15:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PointsTradeListExportRequest implements Serializable {

    private static final long serialVersionUID = -7621844436941926883L;

    /**
     * 查询订单条件参数
     */
    @ApiModelProperty(value = "查询订单条件参数")
    private PointsTradeQueryDTO pointsTradeQueryDTO;
}
