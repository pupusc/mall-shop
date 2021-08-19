package com.wanmi.sbc.order.api.request.refund;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RefundOrderByIdRequest implements Serializable {

    private static final long serialVersionUID = -3009581557118223677L;

    @ApiModelProperty(value = "退款id")
    private String refundId;
}
