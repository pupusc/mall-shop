package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProviderTradeStatusSyncRequest {
    @ApiModelProperty("第三方订单号")
    private String orderId;
    @ApiModelProperty("发货单号")
    private String platformCode;
    @ApiModelProperty("快递公司")
    private String post;
    @ApiModelProperty("快递单号")
    private String postNumber;
    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postDate;
    private Integer orderStatus;
}
