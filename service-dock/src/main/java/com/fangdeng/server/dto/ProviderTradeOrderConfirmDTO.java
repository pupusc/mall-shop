package com.fangdeng.server.dto;

import com.fangdeng.server.enums.DeliveryStatus;
import com.fangdeng.server.vo.DeliveryItemVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProviderTradeOrderConfirmDTO implements Serializable {
    private static final long serialVersionUID = 7948263149837706860L;
    @ApiModelProperty("第三方订单号")
    private String orderId;
    @ApiModelProperty("发货单号")
    private String platformCode;
    private Integer status;
    @ApiModelProperty("下单失败描述")
    private String statusDesc;

}
