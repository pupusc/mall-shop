package com.wanmi.sbc.order.api.request.payorder;

import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePayOrderRequest implements Serializable {

    @ApiModelProperty(value = "支付单id")
    String payOrderId;

    @ApiModelProperty(value = "支付状态")
    PayOrderStatus payOrderStatus;
}
