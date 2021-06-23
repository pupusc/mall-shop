package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单批量审核请求参数结构</p>
 * Created by of628-wenzhi on 2017-11-28-下午4:49.
 */
@ApiModel
@Data
public class TradeAuditBatchRequest extends TradeAuditRequest{
    private static final long serialVersionUID = -5264787137022101807L;

    /**
     * 订单号ids
     */
    @ApiModelProperty(value = "订单号ids")
    private String[] ids;
}
