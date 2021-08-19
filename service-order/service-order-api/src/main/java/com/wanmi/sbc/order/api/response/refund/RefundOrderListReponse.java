package com.wanmi.sbc.order.api.response.refund;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Api
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundOrderListReponse implements Serializable {

    /**
     * 退款记录列表
     */
    @ApiModelProperty(value = "退款记录列表")
    private List<RefundOrderResponse> refundOrderResponseList;
}
