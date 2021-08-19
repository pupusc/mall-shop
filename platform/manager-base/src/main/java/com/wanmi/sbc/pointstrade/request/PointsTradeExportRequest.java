package com.wanmi.sbc.pointstrade.request;

import com.wanmi.sbc.order.bean.dto.PointsTradeQueryDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName pointsTradeExportRequest
 * @Description 积分订单列表导出参数结构
 * @Author lvzhenwei
 * @Date 2019/5/10 15:18
 **/
public class PointsTradeExportRequest extends PointsTradeQueryDTO {

    /**
     * jwt token
     */
    @ApiModelProperty(value = "jwt token")
    private String token;
}
