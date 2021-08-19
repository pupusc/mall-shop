package com.wanmi.sbc.order.api.request.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流水单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundBillDeleteByIdRequest  implements Serializable{
    private static final long serialVersionUID = -8032533726913011315L;

    @ApiModelProperty(value = "退款id")
    private String refundId;
}
