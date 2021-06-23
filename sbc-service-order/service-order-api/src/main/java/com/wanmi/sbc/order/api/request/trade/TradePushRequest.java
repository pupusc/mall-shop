package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradePushRequest {

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    private String tid;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayCallBackType payCallBackType;
}
