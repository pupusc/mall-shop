package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @program: sbc-background
 * @description: ERP订单退款状态修改请求参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 18:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPRefundUpdateRequest extends ERPBaseRequest{

    /**
     * 平台单号
     */
    @JsonProperty("tid")
    private String tid;

    /**
     * 子订单号
     */
    @JsonProperty("oid")
    private String oid;

    /**
     * 退款状态(0:未退款 1:退款完成 2:退款中)
     */
    @JsonProperty("refund_state")
    private int refundState;
}
