package com.wanmi.sbc.linkedmall.api.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SbcCreateOrderAndPayResponse implements Serializable {

    @ApiModelProperty(value = "linkedmall链接")
    private String redirectUrl;

    @ApiModelProperty(value = "linkedmall订单号")
    private List<String> lmOrderList;

    @ApiModelProperty(value = "淘宝订单号")
    private List<String> orderIds;

    @ApiModelProperty(value = "linkedmall支付流水号")
    private List<String> payTradeIds;
}
