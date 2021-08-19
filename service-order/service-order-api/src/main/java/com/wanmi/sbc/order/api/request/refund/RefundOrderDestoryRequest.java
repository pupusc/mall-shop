package com.wanmi.sbc.order.api.request.refund;


import com.wanmi.sbc.common.base.Operator;
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
public class RefundOrderDestoryRequest implements Serializable {

    private static final long serialVersionUID = -3329561533990846776L;

    @ApiModelProperty(value = "退款id")
    private String refundId;

    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
