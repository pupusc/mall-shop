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
public class RefundOrderRefundRequest implements Serializable {

    private static final long serialVersionUID = 7543009525569900842L;

    @ApiModelProperty(value = "退单id")
    private String rid;

    @ApiModelProperty(value = "退款失败原因")
    private String failedReason;

    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
