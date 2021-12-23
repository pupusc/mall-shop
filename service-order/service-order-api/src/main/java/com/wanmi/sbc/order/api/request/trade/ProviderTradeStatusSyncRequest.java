package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProviderTradeStatusSyncRequest implements Serializable {
    private static final long serialVersionUID = -7442690808204461302L;
    @ApiModelProperty("第三方订单号")
    private String orderId;
    @ApiModelProperty("发货单号")
    private String platformCode;
    @ApiModelProperty("下单状态0成功1失败")
    private Integer status;
    @ApiModelProperty("下单失败描述")
    private String statusDesc;


}
