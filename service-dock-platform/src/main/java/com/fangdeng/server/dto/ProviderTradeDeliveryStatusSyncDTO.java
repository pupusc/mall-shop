package com.fangdeng.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProviderTradeDeliveryStatusSyncDTO {
    @ApiModelProperty(value = "订单号")
    private String tid;
}
