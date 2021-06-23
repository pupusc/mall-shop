package com.wanmi.sbc.order.api.request.areas;

import com.wanmi.sbc.order.bean.dto.TradeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class OrderAddRequest implements Serializable {

    @ApiModelProperty(value = "订单信息")
    TradeDTO tradeDTO;
}
