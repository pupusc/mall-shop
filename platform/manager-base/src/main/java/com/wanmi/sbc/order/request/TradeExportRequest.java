package com.wanmi.sbc.order.request;

import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单列表导出参数结构</p>
 * Created by of628-wenzhi on 2017-06-09-上午10:33.
 */
@ApiModel
@Data
public class TradeExportRequest extends TradeQueryDTO {
    // jwt token
    @ApiModelProperty(value = "jwt token")
    private String token;
}
