package com.wanmi.sbc.order.api.request.areas;

import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ReturnOrderInitRequest {

    @ApiModelProperty(value = "退单信息")
    ReturnOrderDTO returnOrderVO;
}
