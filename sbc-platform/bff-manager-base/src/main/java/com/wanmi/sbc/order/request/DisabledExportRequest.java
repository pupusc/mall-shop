package com.wanmi.sbc.order.request;

import com.wanmi.sbc.order.bean.dto.DisabledDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单列表导出参数结构</p>
 * Created by caiping
 */
@ApiModel
@Data
public class DisabledExportRequest extends DisabledDTO {
    // jwt token
    @ApiModelProperty(value = "jwt token")
    private String token;
}
