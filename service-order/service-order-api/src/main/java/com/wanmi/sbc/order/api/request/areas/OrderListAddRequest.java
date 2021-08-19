package com.wanmi.sbc.order.api.request.areas;

import com.wanmi.sbc.order.bean.dto.TradeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class OrderListAddRequest implements Serializable {

    @ApiModelProperty(value = "订单信息列表")
    List<TradeDTO> tradeDTOS;
}
