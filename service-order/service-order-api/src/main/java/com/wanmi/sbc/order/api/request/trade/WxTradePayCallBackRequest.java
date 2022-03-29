package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class WxTradePayCallBackRequest implements Serializable {

    private String orderId;

    private String transationId;
}
